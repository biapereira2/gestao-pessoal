package gestao.pessoal.jpa.aplicacao.checkIn;

import gestao.pessoal.jpa.aplicacao.habito.HabitoSteps;
import gestao.pessoal.jpa.aplicacao.progressoUsuario.FakeProgressoUsuarioService;
import gestao.pessoal.jpa.aplicacao.habito.FakeRepositorioHabito;
import gestao.pessoal.dominio.principal.princ.checkIn.CheckIn;
import gestao.pessoal.dominio.principal.princ.checkIn.CheckInService;
import gestao.pessoal.dominio.principal.princ.habito.Habito;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


// =================================================================
// STEPS DEFINITION (MAPEAMENTO DO GHERKIN)
// =================================================================
public class CheckInSteps {

    private final FakeRepositorioCheckIn fakeRepositorioCheckIn;
    private final FakeProgressoUsuarioService fakeProgressoUsuarioService;
    private final CheckInService checkInService;
    private static CheckInSteps instanciaAtual;

    // Dependência injetada (precisa do HabitoSteps para buscar hábitos e o usuário)
    private final HabitoSteps habitoSteps;

    private Throwable excecaoCapturada;
    private List<CheckIn> checkinsListados;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CheckInSteps(HabitoSteps habitoSteps) {
        instanciaAtual = this;
        this.habitoSteps = habitoSteps;
        this.fakeRepositorioCheckIn = new FakeRepositorioCheckIn();
        this.fakeProgressoUsuarioService = new FakeProgressoUsuarioService();

        // Satisfazendo as 3 dependências do CheckInService
        this.checkInService = new CheckInService(
                fakeRepositorioCheckIn,
                habitoSteps.repositorioHabito, // Repositório de Hábito (acessado via HabitoSteps)
                fakeProgressoUsuarioService   // Fake Service para o Progresso do Usuário
        );
    }

    @Before
    public void setupCenarioCheckIn() {
        // Hook para resetar o estado antes de cada cenário de Check-In
        this.excecaoCapturada = null;
        this.checkinsListados = null;
        this.fakeRepositorioCheckIn.limpar();

        // IMPORTANTE: Limpa o FakeRepositorioHabito para garantir que o GIVEN
        // que_sou_um_usuario_autenticado não traga hábitos de cenários anteriores.
        // Assumimos que o HabitoSteps.repositorioHabito é um FakeRepositorioHabito
        if (habitoSteps.repositorioHabito instanceof FakeRepositorioHabito) {
            ((FakeRepositorioHabito) habitoSteps.repositorioHabito).limpar();
        }
        // OBS: O usuário (habitoSteps.usuario) já deve ser resetado no @Before de HabitoSteps.
    }

    public Throwable getExcecaoCapturada() {
        return excecaoCapturada;
    }

    public static CheckInSteps getInstanciaAtual() {
        return instanciaAtual;
    }


    // =======================================================
    // MÉTODOS AUXILIARES
    // =======================================================

    private UUID buscarHabitoIdPorNome(String nomeHabito) {
        // Acessa o repositório de Hábito do HabitoSteps para encontrar o ID
        return habitoSteps.repositorioHabito.listarTodosPorUsuario(habitoSteps.usuario.getId()).stream()
                .filter(h -> h.getNome().equals(nomeHabito))
                .findFirst()
                .map(Habito::getId)
                .orElseThrow(() -> new IllegalStateException("Hábito '" + nomeHabito + "' não encontrado no repositório fake."));
    }

    private LocalDate parseData(String dataStr) {
        return LocalDate.parse(dataStr, FORMATTER);
    }

    // =======================================================
    // DEFINIÇÕES DOS STEPS (GIVEN/AND)
    // =======================================================

    // Renomeado para refletir a nova GIVEN/AND
    @And("que o dia da ação é {string}")
    public void que_o_dia_da_acao_e(String dataStr) {
        // A data é usada implicitamente no WHEN/THEN
    }

    @And("o hábito {string} já está marcado como feito no dia {string}")
    public void o_habito_ja_esta_marcado_como_feito(String nomeHabito, String dataStr) {
        UUID habitoId = buscarHabitoIdPorNome(nomeHabito);
        LocalDate data = parseData(dataStr);

        try {
            checkInService.marcarCheckIn(habitoSteps.usuario.getId(), habitoId, data);
        } catch (Exception e) {
            // Ignora se for duplicado
        }
    }

    @And("o hábito {string} tem check-ins registrados nos dias {string}")
    public void o_habito_tem_checkins_registrados(String nomeHabito, String diasStr) {
        UUID habitoId = buscarHabitoIdPorNome(nomeHabito);

        // Divide por vírgula e itera sobre as datas
        Stream.of(diasStr.split(","))
                .map(String::trim)
                .forEach(dataStr -> {
                    LocalDate data = parseData(dataStr);
                    try {
                        checkInService.marcarCheckIn(habitoSteps.usuario.getId(), habitoId, data);
                    } catch (Exception e) {
                        // ignora duplicidade
                    }
                });
    }

    // =======================================================
    // DEFINIÇÕES DOS STEPS (WHEN)
    // =======================================================

    // Novo Step para o When do registro de check-in
    @When("eu tento registrar o check-in para o hábito {string} no dia {string}")
    public void eu_tento_registrar_o_checkin_para_o_habito(String nomeHabito, String dataStr) {
        UUID habitoId = buscarHabitoIdPorNome(nomeHabito);
        LocalDate data = parseData(dataStr);
        UUID usuarioId = habitoSteps.usuario.getId();

        try {
            checkInService.marcarCheckIn(usuarioId, habitoId, data);
        } catch (Throwable e) {
            excecaoCapturada = e;
        }
    }

    // Novo Step para o When de remoção de check-in
    @When("eu tento remover o check-in para o hábito {string} no dia {string}")
    public void eu_tento_remover_o_checkin_para_o_habito(String nomeHabito, String dataStr) {
        UUID habitoId = buscarHabitoIdPorNome(nomeHabito);
        LocalDate data = parseData(dataStr);
        UUID usuarioId = habitoSteps.usuario.getId();

        try {
            checkInService.desmarcarCheckIn(usuarioId, habitoId, data);
        } catch (Throwable e) {
            excecaoCapturada = e;
        }
    }

    // Mapeando o novo step de listagem
    @When("eu solicito a lista de check-ins para o hábito {string} no período de {string} a {string}")
    public void eu_solicito_a_lista_de_check_ins_para_o_habito_no_periodo(String nomeHabito, String dataInicialStr, String dataFinalStr) {
        UUID habitoId = buscarHabitoIdPorNome(nomeHabito);

        try {
            // 1. Lista todos os check-ins do hábito
            checkinsListados = checkInService.listarCheckInsPorHabito(habitoSteps.usuario.getId(), habitoId);

            // 2. Simula a filtragem pelo período de calendário (Lógica da camada de aplicação/visualização)
            LocalDate dataInicial = parseData(dataInicialStr);
            LocalDate dataFinal = parseData(dataFinalStr);
            checkinsListados = checkinsListados.stream()
                    .filter(c -> !c.getData().isBefore(dataInicial) && !c.getData().isAfter(dataFinal))
                    .collect(Collectors.toList());

        } catch (Throwable e) {
            excecaoCapturada = e;
        }
    }


    // =======================================================
    // VERIFICAÇÕES (THEN/AND)
    // =======================================================

    @Then("o check-in deve ser registrado com sucesso para o hábito {string} no dia {string}")
    public void o_check_in_deve_ser_registrado_com_sucesso(String nomeHabito, String dataStr) {
        UUID habitoId = buscarHabitoIdPorNome(nomeHabito);
        LocalDate data = parseData(dataStr);

        Assertions.assertNull(excecaoCapturada, "Exceção inesperada do CheckIn: " + excecaoCapturada);
        assertTrue(fakeRepositorioCheckIn.buscarPorHabitoEData(
                habitoId, data, habitoSteps.usuario.getId()
        ).isPresent(), "O check-in não foi encontrado no repositório.");
    }

    @Then("o check-in deve ser removido com sucesso para o hábito {string} no dia {string}")
    public void o_check_in_deve_ser_removido_com_sucesso(String nomeHabito, String dataStr) {
        Assertions.assertNull(excecaoCapturada, "Exceção inesperada na remoção: " + excecaoCapturada);
    }

    @And("o hábito {string} não deve ter check-in registrado no dia {string}")
    public void o_habito_nao_deve_ter_checkin_registrado(String nomeHabito, String dataStr) {
        UUID habitoId = buscarHabitoIdPorNome(nomeHabito);
        LocalDate data = parseData(dataStr);

        Assertions.assertFalse(fakeRepositorioCheckIn.buscarPorHabitoEData(
                habitoId, data, habitoSteps.usuario.getId()
        ).isPresent(), "O check-in não foi removido e ainda está no repositório.");
    }

    @Then("eu devo ver uma lista com {int} check-ins")
    public void eu_devo_ver_uma_lista_com_checkins(int quantidadeEsperada) {
        Assertions.assertNull(excecaoCapturada, "Exceção inesperada na listagem: " + excecaoCapturada);
        assertNotNull(checkinsListados, "A lista de check-ins não foi gerada.");
        Assertions.assertEquals(quantidadeEsperada, checkinsListados.size(), "O número de check-ins listados está incorreto.");
    }

    @Then("eu devo receber um erro de check-in duplicado")
    public void eu_devo_receber_um_erro_de_checkin_duplicado() {
        Throwable e = CheckInSteps.getInstanciaAtual().getExcecaoCapturada();
        assertNotNull(e, "Nenhuma exceção foi lançada, mas uma era esperada.");
        assertTrue(e.getMessage().toLowerCase()
                        .contains("check-in para este hábito já foi registrado"),
                "A mensagem de erro esperada era 'check-in para este hábito já foi registrado', mas foi '" + e.getMessage() + "'");
    }
}