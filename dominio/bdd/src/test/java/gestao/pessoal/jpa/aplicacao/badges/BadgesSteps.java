package gestao.pessoal.jpa.aplicacao.badges;

import gestao.pessoal.jpa.aplicacao.progressoUsuario.FakeProgressoUsuarioService;
import gestao.pessoal.jpa.aplicacao.meta.FakeRepositorioMeta;
import gestao.pessoal.jpa.aplicacao.usuario.FakeRepositorioUsuario;
import gestao.pessoal.dominio.principal.compartilhado.usuario.Usuario;
import gestao.pessoal.dominio.principal.engajamento.badges.BadgesService;
import gestao.pessoal.dominio.principal.engajamento.badges.Badges;
import gestao.pessoal.dominio.principal.princ.meta.Meta;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class BadgesSteps {

    private final BadgesService badgesService;
    private final FakeRepositorioBadges repositorioBadges;
    private final FakeRepositorioMeta repositorioMeta;
    private final FakeProgressoUsuarioService fakeProgressoUsuarioService;
    private final FakeRepositorioUsuario repositorioUsuario;

    private List<Badges> listaResultado;
    private UUID usuarioId;


    public BadgesSteps(FakeProgressoUsuarioService fakeProgressoUsuarioService) {

        this.usuarioId = UUID.randomUUID();

        this.repositorioBadges = new FakeRepositorioBadges();
        this.repositorioMeta = new FakeRepositorioMeta();
        this.repositorioUsuario = new FakeRepositorioUsuario();

        this.fakeProgressoUsuarioService = fakeProgressoUsuarioService;

        Usuario usuario = new Usuario("Usuario Teste", "teste@badges.com", "senha123");
        this.repositorioUsuario.salvar(usuario);

        this.badgesService = new BadgesService(
                this.repositorioBadges,
                this.repositorioMeta,
                this.repositorioUsuario,
                this.fakeProgressoUsuarioService
        );
    }

    @Before
    public void setup() {
        this.repositorioBadges.limpar();
        this.repositorioMeta.limpar();

        this.badgesService.carregarModelosPadrao();
        this.listaResultado = null;

        Usuario usuario = new Usuario("Usuario Teste", "teste@badges.com", "senha123");
        this.usuarioId = usuario.getId();
        this.repositorioUsuario.salvar(usuario);
    }


    @Given("existem badges cadastradas no sistema")
    public void existem_badges_cadastradas_no_sistema() {
        assertTrue(repositorioBadges.listarTodosModelos().size() > 0,
                "Nenhuma badge modelo carregada no repositório.");
    }

    @And("o usuário tem o nível atual {int} no ProgressoUsuario")
    public void o_usuario_tem_o_nivel_atual_no_progresso_usuario(int nivel) {
        this.fakeProgressoUsuarioService.setNivelAtual(usuarioId, nivel);
    }

    @And("o usuário completou {int} metas com sucesso")
    public void o_usuario_completou_metas_com_sucesso(int metasConcluidas) {
        this.repositorioMeta.limpar();

        for (int i = 0; i < metasConcluidas; i++) {

            Meta metaConcluida = new Meta(
                    usuarioId,
                    Meta.Tipo.MENSAL,
                    "Meta Concluída " + i,
                    1,
                    List.of(UUID.randomUUID())      // 🟢 obrigatório agora
            );

            metaConcluida.setHabitosCompletos(1);
            this.repositorioMeta.salvar(metaConcluida);
        }
    }

    @And("existe a badge {string} requerindo {int} metas concluídas")
    public void existe_a_badge_requerindo_metas_concluidas(String nomeBadge, int valorRequerido) {
        Badges badge = new Badges(nomeBadge, "Requer " + valorRequerido + " metas",
                Badges.Categoria.META_ATINGIDA, valorRequerido);

        repositorioBadges.salvarModelo(badge);
    }

    @And("o usuário já conquistou as badges {string}")
    public void o_usuario_ja_conquistou_as_badges(String nomesBadgesStr) {

        List<String> nomes = Arrays.stream(nomesBadgesStr.split(" e "))
                .map(String::trim)
                .collect(Collectors.toList());

        for (String nome : nomes) {

            Badges modelo = repositorioBadges.listarTodosModelos().stream()
                    .filter(b -> b.getNome().equals(nome))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Modelo de badge " + nome + " não encontrado."));

            Badges badgeConquistada = Badges.reidratarConquistada(
                    modelo.getId(),
                    modelo.getNome(),
                    modelo.getDescricao(),
                    modelo.getCategoria(),
                    modelo.getValorRequerido(),
                    usuarioId
            );

            repositorioBadges.salvarConquista(badgeConquistada);
        }
    }

    @And("existem as badges {string} e {string} disponíveis")
    public void existem_as_badges_e_disponiveis(String nome1, String nome2) {
        assertTrue(repositorioBadges.listarTodosModelos().stream()
                        .anyMatch(b -> b.getNome().equals(nome1)),
                "Badge " + nome1 + " não encontrada.");
    }


    @When("o usuário atinge o nível {int} no ProgressoUsuario")
    public void o_usuario_atinge_o_nivel_no_progresso_usuario(int novoNivel) {
        this.fakeProgressoUsuarioService.setNivelAtual(usuarioId, novoNivel);
        badgesService.verificarEConcederBadges(usuarioId);
    }

    @When("o usuário completa sua {int}ª meta com sucesso")
    public void o_usuario_completa_sua_meta_com_sucesso(int metaConcluida) {

        Meta metaConcluidaAtual = new Meta(
                usuarioId,
                Meta.Tipo.MENSAL,
                "Meta Final",
                1,
                List.of(UUID.randomUUID())     // 🟢 obrigatório
        );

        metaConcluidaAtual.setHabitosCompletos(1);
        this.repositorioMeta.salvar(metaConcluidaAtual);

        badgesService.verificarEConcederBadges(usuarioId);
    }

    @When("eu acesso a lista de conquistas")
    public void eu_acesso_a_lista_de_conquistas() {
        listaResultado = badgesService.listarConquistas(usuarioId);
    }

    @When("eu acesso a lista de badges disponíveis")
    public void eu_acesso_a_lista_de_badges_disponiveis() {
        listaResultado = badgesService.listarBadgesDisponiveis(usuarioId);
    }


    @Then("a badge {string} deve ser concedida ao usuário")
    public void a_badge_deve_ser_concedida_ao_usuario(String nomeBadge) {

        UUID modeloId = repositorioBadges.listarTodosModelos().stream()
                .filter(b -> b.getNome().equals(nomeBadge))
                .findFirst()
                .map(Badges::getId)
                .orElseThrow(() -> new IllegalStateException("Modelo da badge " + nomeBadge + " não encontrado."));

        assertTrue(repositorioBadges.usuarioConquistouBadge(usuarioId, modeloId),
                "A badge '" + nomeBadge + "' não foi concedida ao usuário.");
    }

    @And("a lista de conquistas do usuário deve conter {int} badge")
    public void a_lista_de_conquistas_do_usuario_deve_conter_badge(int quantidade) {
        assertEquals(quantidade, repositorioBadges.listarConquistasPorUsuario(usuarioId).size());
    }

    @And("a lista de conquistas do usuário deve conter a badge {string}")
    public void a_lista_de_conquistas_do_usuario_deve_conter_a_badge(String nomeBadge) {
        assertTrue(
                repositorioBadges.listarConquistasPorUsuario(usuarioId)
                        .stream().anyMatch(b -> b.getNome().equals(nomeBadge))
        );
    }

    @Then("eu devo ver uma lista com {int} badges conquistadas")
    public void eu_devo_ver_uma_lista_com_badges_conquistadas(int quantidade) {
        assertNotNull(listaResultado);
        assertEquals(quantidade, listaResultado.size());
    }

    @Then("eu devo ver uma lista com {int} badges a desbloquear")
    public void eu_devo_ver_uma_lista_com_badges_a_desbloquear(int quantidade) {
        assertNotNull(listaResultado);
        assertEquals(quantidade, listaResultado.size());
    }

    @And("a lista não deve conter a badge {string}")
    public void a_lista_nao_deve_conter_a_badge(String nomeBadge) {
        assertFalse(listaResultado.stream().anyMatch(b -> b.getNome().equals(nomeBadge)));
    }
}
