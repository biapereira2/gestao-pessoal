package gestao.pessoal.aplicacao;

import gestao.pessoal.compartilhado.Usuario;
import gestao.pessoal.habito.Habito;
import gestao.pessoal.habito.RepositorioHabito;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class HabitoSteps {

    private HabitoService habitoService;
    private RepositorioHabito repositorioHabito;
    private Usuario usuario;
    private Exception excecaoLancada;

    // Este método é executado antes de cada cenário
    public HabitoSteps() {
        this.repositorioHabito = new HabitoRepositorioEmMemoria();
        this.habitoService = new HabitoService(this.repositorioHabito);
        this.excecaoLancada = null; // Reseta a exceção para cada cenário
    }

    // --- DADO (Given) ---
    @Dado("que sou um usuário autenticado")
    public void que_sou_um_usuario_autenticado() {
        this.usuario = new Usuario("Usuário Teste", "teste@email.com", "123456");
    }

    @Dado("eu já possuo um hábito cadastrado com o nome {string}")
    public void eu_ja_possuo_um_habito_cadastrado_com_o_nome(String nomeHabito) {
        habitoService.criar(this.usuario.getId(), nomeHabito, "desc", "cat", "freq");
    }

    @Dado("eu já possuo os hábitos {string} e {string} cadastrados")
    public void eu_ja_possuo_os_habitos_e_cadastrados(String habito1, String habito2) {
        habitoService.criar(this.usuario.getId(), habito1, "desc1", "cat1", "freq1");
        habitoService.criar(this.usuario.getId(), habito2, "desc2", "cat2", "freq2");
    }

    // --- QUANDO (When) ---
    @Quando("eu tento criar um hábito chamado {string}")
    public void eu_tento_criar_um_habito_chamado(String nomeHabito) {
        try {
            habitoService.criar(this.usuario.getId(), nomeHabito, "desc", "cat", "freq");
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @Quando("eu tento criar um hábito com o nome em branco")
    public void eu_tento_criar_um_habito_com_o_nome_em_branco() {
        try {
            habitoService.criar(this.usuario.getId(), "", "desc", "cat", "freq");
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @Quando("eu atualizo o nome do hábito {string} para {string}")
    public void eu_atualizo_o_nome_do_habito_para(String nomeAntigo, String nomeNovo) {
        Habito habito = repositorioHabito.listarTodosPorUsuario(usuario.getId()).stream()
                .filter(h -> h.getNome().equals(nomeAntigo))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Hábito '"+ nomeAntigo +"' não encontrado para atualizar."));
        habitoService.atualizar(habito.getId(), nomeNovo, "desc nova", "cat nova");
    }

    @Quando("eu tento atualizar o nome do hábito {string} para um nome em branco")
    public void eu_tento_atualizar_o_nome_do_habito_para_um_nome_em_branco(String nomeAntigo) {
        Habito habito = repositorioHabito.listarTodosPorUsuario(usuario.getId()).stream()
                .filter(h -> h.getNome().equals(nomeAntigo))
                .findFirst().get();
        try {
            habitoService.atualizar(habito.getId(), "", "desc", "cat");
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @Quando("eu tento atualizar o nome de um hábito inexistente com id {string} para {string}")
    public void eu_tento_atualizar_o_nome_de_um_habito_inexistente_com_id_para(String id, String nomeNovo) {
        try {
            habitoService.atualizar(UUID.fromString(id), nomeNovo, "desc", "cat");
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @Quando("eu excluo o hábito {string}")
    public void eu_excluo_o_habito(String nomeHabito) {
        Habito habito = repositorioHabito.listarTodosPorUsuario(usuario.getId()).stream()
                .filter(h -> h.getNome().equals(nomeHabito))
                .findFirst().get();
        habitoService.excluir(habito.getId());
    }

    @Quando("eu tento excluir um hábito inexistente com id {string}")
    public void eu_tento_excluir_um_habito_inexistente_com_id(String id) {
        try {
            habitoService.excluir(UUID.fromString(id));
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @Quando("eu acesso a minha lista de hábitos")
    public void eu_acesso_a_minha_lista_de_habitos() {
        // Ação vazia, a verificação será feita no "Então"
    }

    // --- ENTÃO (Then) ---
    @Entao("o hábito {string} deve estar na minha lista de hábitos")
    public void o_habito_deve_estar_na_minha_lista_de_habitos(String nomeHabito) {
        List<Habito> habitos = habitoService.listarPorUsuario(this.usuario.getId());
        boolean encontrado = habitos.stream().anyMatch(h -> h.getNome().equals(nomeHabito));
        assertTrue(encontrado, "O hábito '" + nomeHabito + "' não foi encontrado na lista.");
    }

    // ESTE É O MÉTODO REUTILIZÁVEL PARA TODOS OS ERROS
    @Entao("eu devo receber um erro informando que {string}")
    public void eu_devo_receber_um_erro_informando_que(String mensagemDeErro) {
        assertNotNull(excecaoLancada, "Nenhuma exceção foi lançada, mas uma era esperada.");
        assertTrue(excecaoLancada.getMessage().toLowerCase().contains(mensagemDeErro.toLowerCase()),
                "A mensagem de erro esperada era '" + mensagemDeErro + "', mas foi '" + excecaoLancada.getMessage() + "'");
    }

    @Entao("a minha lista de hábitos deve estar vazia")
    public void a_minha_lista_de_habitos_deve_estar_vazia() {
        assertEquals(0, habitoService.listarPorUsuario(usuario.getId()).size());
    }

    @Entao("a minha lista de hábitos deve conter apenas {int} hábito")
    public void a_minha_lista_de_habitos_deve_conter_apenas_habito(Integer quantidade) {
        assertEquals(quantidade, habitoService.listarPorUsuario(usuario.getId()).size());
    }

    @Entao("o hábito {string} não deve mais existir")
    public void o_habito_nao_deve_mais_existir(String nomeHabito) {
        boolean encontrado = habitoService.listarPorUsuario(usuario.getId())
                .stream().anyMatch(h -> h.getNome().equals(nomeHabito));
        assertFalse(encontrado);
    }

    @Entao("o hábito {string} deve continuar na minha lista")
    public void o_habito_deve_continuar_na_minha_lista(String nomeHabito) {
        boolean encontrado = habitoService.listarPorUsuario(usuario.getId())
                .stream().anyMatch(h -> h.getNome().equals(nomeHabito));
        assertTrue(encontrado);
    }

    @Entao("o hábito {string} não deve mais ser exibido na minha lista de hábitos")
    public void o_habito_nao_deve_mais_ser_exibido_na_minha_lista_de_habitos(String nomeHabito) {
        boolean encontrado = habitoService.listarPorUsuario(usuario.getId())
                .stream().anyMatch(h -> h.getNome().equals(nomeHabito));
        assertFalse(encontrado);
    }

    @Entao("a lista deve conter exatamente {int} hábitos")
    public void a_lista_deve_conter_exatamente_habitos(Integer quantidade) {
        assertEquals(quantidade, habitoService.listarPorUsuario(usuario.getId()).size());
    }

    @E("a lista deve incluir {string} e {string}")
    public void a_lista_deve_incluir_e(String habito1, String habito2) {
        List<String> nomesDosHabitos = habitoService.listarPorUsuario(usuario.getId())
                .stream().map(Habito::getNome).toList();
        assertTrue(nomesDosHabitos.containsAll(List.of(habito1, habito2)));
    }
}