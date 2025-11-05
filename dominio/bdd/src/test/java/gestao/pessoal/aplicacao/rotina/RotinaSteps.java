package gestao.pessoal.aplicacao.rotina;

import gestao.pessoal.usuario.Usuario;
import gestao.pessoal.habito.rotina.Rotina;
import gestao.pessoal.habito.rotina.RepositorioRotina;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.E;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class RotinaSteps {

    private RotinaService rotinaService;
    private RepositorioRotina repositorioRotina;
    private Usuario usuario;
    private Exception excecaoLancada;

    public RotinaSteps() {
        this.repositorioRotina = new FakeRepositorioRotina();
        this.rotinaService = new RotinaService(this.repositorioRotina);
        this.excecaoLancada = null;
    }


    @Dado("que sou um usuário autenticado para rotinas")
    public void que_sou_um_usuario_autenticado_para_rotinas() {
        this.usuario = new Usuario("Usuário Rotina", "rotina@email.com", "123456");
    }

    @Dado("que eu possuo uma rotina chamada {string} com descrição {string}")
    public void que_eu_possuo_uma_rotina_chamada_com_descricao(String nome, String descricao) {
        rotinaService.criarRotina(usuario.getId(), nome, descricao, List.of());
    }

    @Dado("que eu possuo as rotinas {string} e {string} cadastradas")
    public void que_eu_possuo_as_rotinas_e_cadastradas(String rotina1, String rotina2) {
        rotinaService.criarRotina(usuario.getId(), rotina1, "desc1", List.of());
        rotinaService.criarRotina(usuario.getId(), rotina2, "desc2", List.of());
    }

    @Quando("eu tento criar uma rotina chamada {string}")
    public void eu_tento_criar_uma_rotina_chamada(String nomeRotina) {
        try {
            rotinaService.criarRotina(usuario.getId(), nomeRotina, "desc", List.of());
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @Quando("eu tento criar uma rotina com o nome em branco")
    public void eu_tento_criar_uma_rotina_com_o_nome_em_branco() {
        try {
            rotinaService.criarRotina(usuario.getId(), "", "desc", List.of());
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @Quando("eu atualizo o nome da rotina {string} para {string}")
    public void eu_atualizo_o_nome_da_rotina_para(String nomeAntigo, String nomeNovo) {
        Rotina rotina = repositorioRotina.listarTodosPorUsuario(usuario.getId()).stream()
                .filter(r -> r.getNome().equals(nomeAntigo))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Rotina '" + nomeAntigo + "' não encontrada."));
        rotinaService.atualizarRotina(rotina.getId(), nomeNovo, "nova desc", rotina.getHabitosIds());
    }

    @Quando("eu tento atualizar o nome da rotina {string} para um nome em branco")
    public void eu_tento_atualizar_o_nome_da_rotina_para_um_nome_em_branco(String nomeAntigo) {
        Rotina rotina = repositorioRotina.listarTodosPorUsuario(usuario.getId()).stream()
                .filter(r -> r.getNome().equals(nomeAntigo))
                .findFirst().get();
        try {
            rotinaService.atualizarRotina(rotina.getId(), "", "desc", rotina.getHabitosIds());
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @Quando("eu excluo a rotina {string}")
    public void eu_excluo_a_rotina(String nomeRotina) {
        Rotina rotina = repositorioRotina.listarTodosPorUsuario(usuario.getId()).stream()
                .filter(r -> r.getNome().equals(nomeRotina))
                .findFirst().get();
        rotinaService.deletarRotina(rotina.getId());
    }

    @Quando("eu tento excluir uma rotina inexistente com id {string}")
    public void eu_tento_excluir_uma_rotina_inexistente_com_id(String id) {
        try {
            rotinaService.deletarRotina(UUID.fromString(id));
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @Quando("eu acesso minha lista de rotinas")
    public void eu_acesso_minha_lista_de_rotinas() {
        // step sem ação, apenas para semântica
    }

    @Entao("a rotina {string} deve estar na minha lista de rotinas")
    public void a_rotina_deve_estar_na_minha_lista_de_rotinas(String nomeRotina) {
        List<Rotina> rotinas = rotinaService.listarRotinasDoUsuario(usuario.getId());
        boolean encontrado = rotinas.stream().anyMatch(r -> r.getNome().equals(nomeRotina));
        assertTrue(encontrado, "A rotina '" + nomeRotina + "' não foi encontrada.");
    }

    @Entao("a rotina {string} não deve mais existir")
    public void a_rotina_nao_deve_mais_existir(String nomeRotina) {
        boolean existe = rotinaService.listarRotinasDoUsuario(usuario.getId())
                .stream().anyMatch(r -> r.getNome().equals(nomeRotina));
        assertFalse(existe);
    }

    @Entao("a lista de rotinas deve conter exatamente {int} rotinas")
    public void a_lista_de_rotinas_deve_conter_exatamente_rotinas(Integer quantidade) {
        assertEquals(quantidade, rotinaService.listarRotinasDoUsuario(usuario.getId()).size());
    }

    @Entao("eu devo receber um erro de rotina informando que {string}")
    public void eu_devo_receber_um_erro_de_rotina_informando_que(String mensagemDeErro) {
        assertNotNull(excecaoLancada, "Nenhuma exceção foi lançada, mas uma era esperada.");
        assertTrue(excecaoLancada.getMessage().toLowerCase().contains(mensagemDeErro.toLowerCase()),
                "Esperado erro contendo '" + mensagemDeErro + "', mas foi: " + excecaoLancada.getMessage());
    }

    @E("a lista de rotinas deve incluir {string} e {string}")
    public void a_lista_de_rotinas_deve_incluir_e(String rotina1, String rotina2) {
        List<String> nomes = rotinaService.listarRotinasDoUsuario(usuario.getId())
                .stream().map(Rotina::getNome).toList();
        assertTrue(nomes.containsAll(List.of(rotina1, rotina2)));
    }
}
