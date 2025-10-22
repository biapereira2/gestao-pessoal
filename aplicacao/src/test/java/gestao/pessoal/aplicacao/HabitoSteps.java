package gestao.pessoal.aplicacao;

// Importa a classe real do domínio compartilhado
import gestao.pessoal.compartilhado.Usuario;
// Importa o Service/Repositorio do dominio Habito
import gestao.pessoal.habito.Habito;
import gestao.pessoal.habito.RepositorioHabito;
import io.cucumber.java.Before; // Hook para limpeza
import io.cucumber.java.en.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


// =================================================================
// STEPS DEFINITION (MAPEAMENTO DO GHERKIN)
// =================================================================
public class HabitoSteps {

    // Mantemos como público para que CheckInSteps possa acessá-los
    public HabitoService habitoService;
    public RepositorioHabito repositorioHabito;
    public Usuario usuario; // Agora é a classe Usuario real do domínio compartilhado
    public Exception excecaoLancada;

    // Construtor: Inicializa o Service e o Repositório Mock
    public HabitoSteps() {
        this.repositorioHabito = new FakeRepositorioHabito();
        this.habitoService = new HabitoService(this.repositorioHabito);
    }

    // HOOK: Garante que o estado seja limpo antes de cada cenário
    @Before
    public void setupCenario() {
        // Limpa o repositório para isolar cada teste
        ((FakeRepositorioHabito) this.repositorioHabito).limpar();

        // Reseta a exceção
        this.excecaoLancada = null;

        // CRIAÇÃO DO USUÁRIO: Utilizando o construtor do DOMÍNIO COMPARTILHADO.
        // O ID do usuário será gerado aleatoriamente no construtor do Usuario.
        this.usuario = new Usuario("Usuário Teste", "teste@email.com", "senha123");
    }

    // --- DADO (Given) ---
    @Given("que sou um usuário autenticado")
    public void que_sou_um_usuario_autenticado() {
        // O usuário já foi criado no @Before, garantindo que o ID está disponível
        assertNotNull(this.usuario.getId(), "O ID do usuário autenticado não pode ser nulo.");
    }

    @Given("eu já possuo um hábito cadastrado com o nome {string}")
    public void eu_ja_possuo_um_habito_cadastrado_com_o_nome(String nomeHabito) {
        // O Service.criar já chama o repositorio.salvar, o que é o correto
        habitoService.criar(this.usuario.getId(), nomeHabito, "desc", "cat", "Diaria");
    }

    @Given("eu já possuo os hábitos {string} e {string} cadastrados")
    public void eu_ja_possuo_os_habitos_e_cadastrados(String habito1, String habito2) {
        habitoService.criar(this.usuario.getId(), habito1, "desc1", "cat1", "Diaria");
        habitoService.criar(this.usuario.getId(), habito2, "desc2", "cat2", "Semanal");
    }

    // --- QUANDO (When) ---
    @When("eu tento criar um hábito chamado {string}")
    public void eu_tento_criar_um_habito_chamado(String nomeHabito) {
        try {
            // Chama apenas o Service.criar
            habitoService.criar(this.usuario.getId(), nomeHabito, "desc", "cat", "Diaria");
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @When("eu tento criar um hábito com o nome em branco")
    public void eu_tento_criar_um_habito_com_o_nome_em_branco() {
        try {
            habitoService.criar(this.usuario.getId(), "", "desc", "cat", "Diaria");
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @When("eu atualizo o nome do hábito {string} para {string}")
    public void eu_atualizo_o_nome_do_habito_para(String nomeAntigo, String nomeNovo) {
        Habito habito = repositorioHabito.listarTodosPorUsuario(usuario.getId()).stream()
                .filter(h -> h.getNome().equals(nomeAntigo))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Hábito '"+ nomeAntigo +"' não encontrado para atualizar."));
        // Passando os dados originais para o Service
        habitoService.atualizar(habito.getId(), nomeNovo, habito.getDescricao(), habito.getCategoria(), habito.getFrequencia());
    }

    @When("eu tento atualizar o nome do hábito {string} para um nome em branco")
    public void eu_tento_atualizar_o_nome_do_habito_para_um_nome_em_branco(String nomeAntigo) {
        Habito habito = repositorioHabito.listarTodosPorUsuario(usuario.getId()).stream()
                .filter(h -> h.getNome().equals(nomeAntigo))
                .findFirst().get();
        try {
            habitoService.atualizar(habito.getId(), "", "desc", "cat", "Diaria");
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @When("eu tento atualizar o nome de um hábito inexistente com id {string} para {string}")
    public void eu_tento_atualizar_o_nome_de_um_habito_inexistente_com_id_para(String id, String nomeNovo) {
        try {
            habitoService.atualizar(UUID.fromString(id), nomeNovo, "desc", "cat", "Diaria");
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @When("eu excluo o hábito {string}")
    public void eu_excluo_o_habito(String nomeHabito) {
        Habito habito = repositorioHabito.listarTodosPorUsuario(usuario.getId()).stream()
                .filter(h -> h.getNome().equals(nomeHabito))
                .findFirst().get();
        habitoService.excluir(habito.getId());
    }

    @When("eu tento excluir um hábito inexistente com id {string}")
    public void eu_tento_excluir_um_habito_inexistente_com_id(String id) {
        try {
            habitoService.excluir(UUID.fromString(id));
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @When("eu acesso a minha lista de hábitos")
    public void eu_acesso_a_minha_lista_de_habitos() {
        // Ação vazia, a verificação será feita no "Então"
    }

    // --- ENTÃO (Then) ---
    @Then("o hábito {string} deve estar na minha lista de hábitos")
    public void o_habito_deve_estar_na_minha_lista_de_habitos(String nomeHabito) {
        List<Habito> habitos = habitoService.listarPorUsuario(this.usuario.getId());
        boolean encontrado = habitos.stream().anyMatch(h -> h.getNome().equals(nomeHabito));
        assertTrue(encontrado, "O hábito '" + nomeHabito + "' não foi encontrado na lista.");
    }

    @Then("eu devo receber um erro informando que {string}")
    public void eu_devo_receber_um_erro_informando_que(String mensagemDeErro) {
        assertNotNull(excecaoLancada, "Nenhuma exceção foi lançada, mas uma era esperada.");
        // Aumenta a tolerância para a busca de parte da mensagem
        assertTrue(excecaoLancada.getMessage().toLowerCase().contains(mensagemDeErro.toLowerCase()),
                "A mensagem de erro esperada era '" + mensagemDeErro + "', mas foi '" + excecaoLancada.getMessage() + "'");
    }

    @Then("a minha lista de hábitos deve estar vazia")
    public void a_minha_lista_de_habitos_deve_estar_vazia() {
        assertEquals(0, habitoService.listarPorUsuario(usuario.getId()).size());
    }

    @Then("a minha lista de hábitos deve conter apenas {int} hábito")
    public void a_minha_lista_de_habitos_deve_conter_apenas_habito(Integer quantidade) {
        assertEquals(quantidade, habitoService.listarPorUsuario(usuario.getId()).size());
    }

    @Then("o hábito {string} não deve mais existir")
    public void o_habito_nao_deve_mais_existir(String nomeHabito) {
        boolean encontrado = habitoService.listarPorUsuario(usuario.getId())
                .stream().anyMatch(h -> h.getNome().equals(nomeHabito));
        assertFalse(encontrado);
    }

    @Then("o hábito {string} deve continuar na minha lista")
    public void o_habito_deve_continuar_na_minha_lista(String nomeHabito) {
        boolean encontrado = habitoService.listarPorUsuario(usuario.getId())
                .stream().anyMatch(h -> h.getNome().equals(nomeHabito));
        assertTrue(encontrado);
    }

    @Then("o hábito {string} não deve mais ser exibido na minha lista de hábitos")
    public void o_habito_nao_deve_mais_ser_exibido_na_minha_lista_de_habitos(String nomeHabito) {
        boolean encontrado = habitoService.listarPorUsuario(usuario.getId())
                .stream().anyMatch(h -> h.getNome().equals(nomeHabito));
        assertFalse(encontrado);
    }

    @Then("a lista deve conter exatamente {int} hábitos")
    public void a_lista_deve_conter_exatamente_habitos(Integer quantidade) {
        assertEquals(quantidade, habitoService.listarPorUsuario(usuario.getId()).size());
    }

    @Then("a lista deve incluir {string} e {string}")
    public void a_lista_deve_incluir_e(String habito1, String habito2) {
        List<String> nomesDosHabitos = habitoService.listarPorUsuario(usuario.getId())
                .stream().map(Habito::getNome).toList();
        assertTrue(nomesDosHabitos.containsAll(List.of(habito1, habito2)));
    }
}