package gestao.pessoal.aplicacao;

import gestao.pessoal.aplicacao.FakeProgressoUsuarioService;
import gestao.pessoal.compartilhado.RepositorioUsuario;
import gestao.pessoal.compartilhado.Usuario;
import gestao.pessoal.engajamento.Badges;
import gestao.pessoal.engajamento.RepositorioBadges;
import gestao.pessoal.habito.Meta;
import gestao.pessoal.habito.RepositorioMeta;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


// =================================================================
// IMPLEMENTAÇÃO MOCK (FAKE REPOSITÓRIO BADGES)
// Mantido, mas movido para fora da classe Steps
// =================================================================
class FakeRepositorioBadges implements RepositorioBadges {

    private final Map<UUID, Badges> modelos = new HashMap<>();
    private final Map<UUID, List<Badges>> conquistas = new HashMap<>();

    @Override
    public void salvarModelo(Badges badgeModelo) {
        modelos.put(badgeModelo.getId(), badgeModelo);
    }

    @Override
    public Optional<Badges> buscarModeloPorId(UUID id) {
        // Assume-se que o ID é o mesmo para o modelo e a conquista
        return modelos.values().stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    @Override
    public List<Badges> listarTodosModelos() {
        return new ArrayList<>(modelos.values());
    }

    @Override
    public void salvarConquista(Badges badgeConquistada) {
        conquistas.computeIfAbsent(badgeConquistada.getUsuarioId(), k -> new ArrayList<>()).add(badgeConquistada);
    }

    @Override
    public List<Badges> listarConquistasPorUsuario(UUID usuarioId) {
        return conquistas.getOrDefault(usuarioId, Collections.emptyList());
    }

    @Override
    public boolean usuarioConquistouBadge(UUID usuarioId, UUID badgeModeloId) {
        return conquistas.getOrDefault(usuarioId, Collections.emptyList()).stream()
                .anyMatch(c -> c.getId().equals(badgeModeloId));
    }

    public void limpar() {
        modelos.clear();
        conquistas.clear();
    }
}


// =================================================================
// STEPS DEFINITION (MAPEAMENTO DO GHERKIN)
// =================================================================
public class BadgesSteps {

    // Instâncias de Services e Repositórios
    private final BadgesService badgesService;
    private final FakeRepositorioBadges repositorioBadges;
    private final FakeRepositorioMeta repositorioMeta;
    private final FakeProgressoUsuarioService fakeProgressoUsuarioService;
    private final FakeRepositorioUsuario repositorioUsuario;

    // Estado do Cenário
    private List<Badges> listaResultado;
    private UUID usuarioId;


    public BadgesSteps(FakeProgressoUsuarioService fakeProgressoUsuarioService) {
        // 1. Inicializa o estado fixo do usuário de teste
        this.usuarioId = UUID.randomUUID();

        // 2. Inicializa os Repositórios FAKES necessários (agora locais)
        this.repositorioBadges = new FakeRepositorioBadges();
        this.repositorioMeta = new FakeRepositorioMeta();
        this.repositorioUsuario = new FakeRepositorioUsuario();

        // 3. Mantém o FakeProgressoUsuarioService injetado do seu respectivo Steps
        this.fakeProgressoUsuarioService = fakeProgressoUsuarioService;

        // 4. Salva o usuário no repositório fake (simulando "usuário autenticado")
        Usuario usuario = new Usuario("Usuario Teste", "teste@badges.com", "senha123");
        this.repositorioUsuario.salvar(usuario);

        // 5. Inicializa o Service com as dependências
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

        // Carrega modelos iniciais para cada teste
        this.badgesService.carregarModelosPadrao();
        this.listaResultado = null;

        // Garante que o usuário salvo tenha o ID correto no repositório de usuário
        Usuario usuario = new Usuario("Usuario Teste", "teste@badges.com", "senha123");
        this.usuarioId = usuario.getId();
        this.repositorioUsuario.salvar(usuario);
    }

    // =======================================================
    // GIVEN
    // =======================================================

    // Nota: O step "que sou um usuário autenticado" é atendido pelo setup local (this.usuarioId)

    @Given("existem badges cadastradas no sistema")
    public void existem_badges_cadastradas_no_sistema() {
        assertTrue(repositorioBadges.listarTodosModelos().size() > 0,
                "Nenhuma badge modelo carregada no repositório.");
    }

    @And("o usuário tem o nível atual {int} no ProgressoUsuario")
    public void o_usuario_tem_o_nivel_atual_no_progresso_usuario(int nivel) {
        // Simula o nível do usuário no Fake ProgressoService injetado
        this.fakeProgressoUsuarioService.setNivelAtual(usuarioId, nivel);
    }

    @And("o usuário completou {int} metas com sucesso")
    public void o_usuario_completou_metas_com_sucesso(int metasConcluidas) {
        // Limpa e simula as metas concluídas no FakeRepositorioMeta local
        this.repositorioMeta.limpar();
        for (int i = 0; i < metasConcluidas; i++) {
            // Cria uma meta de exemplo e a marca como concluída (quantidade = habitosCompletos)
            // Nota: O primeiro parâmetro (habitoId) pode ser null/dummy para este teste
            Meta metaConcluida = new Meta(usuarioId, null, Meta.Tipo.MENSAL, "Meta Concluída " + i, 1);
            metaConcluida.setHabitosCompletos(1); // Marcando como completa (1 de 1)
            this.repositorioMeta.salvar(metaConcluida);
        }
    }

    @And("existe a badge {string} requerindo {int} metas concluídas")
    public void existe_a_badge_requerindo_metas_concluidas(String nomeBadge, int valorRequerido) {
        // Garante que o modelo de teste esteja presente (além dos padrões)
        Badges badge = new Badges(nomeBadge, "Requer " + valorRequerido + " metas", Badges.Categoria.META_ATINGIDA, valorRequerido);
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
                    .orElseThrow(() -> new IllegalStateException("Modelo de badge " + nome + " não encontrado para setup."));

            // Concede a badge ao usuário
            repositorioBadges.salvarConquista(Badges.conceder(modelo, usuarioId));
        }
    }

    @And("existem as badges {string} e {string} disponíveis")
    public void existem_as_badges_e_disponiveis(String nome1, String nome2) {
        // Este step apenas garante que os modelos de setup estejam carregados (já feito no @Before)
        assertTrue(repositorioBadges.listarTodosModelos().stream()
                        .anyMatch(b -> b.getNome().equals(nome1)),
                "Badge " + nome1 + " não está cadastrada.");
    }


    // =======================================================
    // WHEN
    // =======================================================

    @When("o usuário atinge o nível {int} no ProgressoUsuario")
    public void o_usuario_atinge_o_nivel_no_progresso_usuario(int novoNivel) {
        // Simula a subida de nível
        this.fakeProgressoUsuarioService.setNivelAtual(usuarioId, novoNivel);

        // Aciona o mecanismo de verificação de badges
        badgesService.verificarEConcederBadges(usuarioId);
    }

    @When("o usuário completa sua {int}ª meta com sucesso")
    public void o_usuario_completa_sua_meta_com_sucesso(int metaConcluida) {
        // Adiciona a meta que falta no FakeRepositorioMeta local
        Meta metaConcluidaAtual = new Meta(usuarioId, null, Meta.Tipo.MENSAL, "Meta Final", 1);
        metaConcluidaAtual.setHabitosCompletos(1);
        this.repositorioMeta.salvar(metaConcluidaAtual);

        // Aciona o mecanismo de verificação de badges
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

    // =======================================================
    // THEN
    // =======================================================

    @Then("a badge {string} deve ser concedida ao usuário")
    public void a_badge_deve_ser_concedida_ao_usuario(String nomeBadge) {
        // Encontra o modelo para obter o ID e verificar a conquista
        UUID modeloId = repositorioBadges.listarTodosModelos().stream()
                .filter(b -> b.getNome().equals(nomeBadge))
                .findFirst()
                .map(Badges::getId)
                .orElseThrow(() -> new IllegalStateException("Modelo de badge " + nomeBadge + " não encontrado."));

        assertTrue(repositorioBadges.usuarioConquistouBadge(usuarioId, modeloId),
                "A badge '" + nomeBadge + "' não foi encontrada nas conquistas do usuário.");
    }

    @And("a lista de conquistas do usuário deve conter {int} badge")
    public void a_lista_de_conquistas_do_usuario_deve_conter_badge(int quantidade) {
        assertEquals(quantidade, repositorioBadges.listarConquistasPorUsuario(usuarioId).size(),
                "A quantidade de badges conquistadas está incorreta.");
    }

    @And("a lista de conquistas do usuário deve conter a badge {string}")
    public void a_lista_de_conquistas_do_usuario_deve_conter_a_badge(String nomeBadge) {
        assertTrue(repositorioBadges.listarConquistasPorUsuario(usuarioId).stream()
                        .anyMatch(b -> b.getNome().equals(nomeBadge)),
                "A badge '" + nomeBadge + "' não foi encontrada na lista de conquistas.");
    }

    @Then("eu devo ver uma lista com {int} badges conquistadas")
    public void eu_devo_ver_uma_lista_com_badges_conquistadas(int quantidade) {
        assertNotNull(listaResultado, "A lista de resultados não foi inicializada.");
        assertEquals(quantidade, listaResultado.size(), "A quantidade de badges listadas está incorreta.");
    }

    @Then("eu devo ver uma lista com {int} badges a desbloquear")
    public void eu_devo_ver_uma_lista_com_badges_a_desbloquear(int quantidade) {
        assertNotNull(listaResultado, "A lista de resultados não foi inicializada.");
        assertEquals(quantidade, listaResultado.size(), "A quantidade de badges a desbloquear está incorreta.");
    }

    @And("a lista não deve conter a badge {string}")
    public void a_lista_nao_deve_conter_a_badge(String nomeBadge) {
        assertFalse(listaResultado.stream().anyMatch(b -> b.getNome().equals(nomeBadge)),
                "A lista de badges disponíveis não deveria conter a badge '" + nomeBadge + "'.");
    }
}