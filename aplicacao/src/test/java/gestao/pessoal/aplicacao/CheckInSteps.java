package gestao.pessoal.aplicacao;

import gestao.pessoal.habito.CheckIn;
import gestao.pessoal.habito.Habito;
import gestao.pessoal.habito.RepositorioCheckIn;

// Imports das anotações em INGLÊS, conforme o arquivo .feature
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


// =================================================================
// IMPLEMENTAÇÃO MOCK (FAKE REPOSITÓRIO)
// =================================================================
class FakeRepositorioCheckIn implements RepositorioCheckIn {

    private final Map<UUID, CheckIn> storage = new ConcurrentHashMap<>();

    @Override
    public void salvar(CheckIn checkIn) {
        storage.put(checkIn.getId(), checkIn);
    }

    @Override
    public void remover(UUID habitoId, LocalDate data, UUID usuarioId) {
        Optional<CheckIn> checkInParaRemover = storage.values().stream()
                .filter(c -> c.getHabitoId().equals(habitoId))
                .filter(c -> c.getData().equals(data))
                .filter(c -> c.getUsuarioId().equals(usuarioId))
                .findFirst();

        checkInParaRemover.ifPresent(checkIn -> storage.remove(checkIn.getId()));
    }

    @Override
    public Optional<CheckIn> buscarPorHabitoEData(UUID habitoId, LocalDate data, UUID usuarioId) {
        return storage.values().stream()
                .filter(c -> c.getHabitoId().equals(habitoId))
                .filter(c -> c.getData().equals(data))
                .filter(c -> c.getUsuarioId().equals(usuarioId))
                .findFirst();
    }

    @Override
    public List<CheckIn> listarPorHabito(UUID habitoId, UUID usuarioId) {
        return storage.values().stream()
                .filter(c -> c.getHabitoId().equals(habitoId))
                .filter(c -> c.getUsuarioId().equals(usuarioId))
                .collect(Collectors.toList());
    }

    public void limpar() {
        storage.clear();
    }
}


// =================================================================
// STEPS DEFINITION (MAPEAMENTO DO GHERKIN)
// Contém APENAS passos relacionados à lógica de Check-In
// =================================================================
public class CheckInSteps {

    private final FakeRepositorioCheckIn fakeRepositorioCheckIn;
    private final CheckInService checkInService;
    private static CheckInSteps instanciaAtual;

    // Dependência injetada: Requer que HabitoSteps tenha campos públicos para acesso
    private final HabitoSteps habitoSteps;

    private Throwable excecaoCapturada;
    private List<CheckIn> checkinsListados;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CheckInSteps(HabitoSteps habitoSteps) {
        instanciaAtual = this;
        this.habitoSteps = habitoSteps;
        this.fakeRepositorioCheckIn = new FakeRepositorioCheckIn();
        this.checkInService = new CheckInService(fakeRepositorioCheckIn);

        this.excecaoCapturada = null;
        this.checkinsListados = null;
        this.fakeRepositorioCheckIn.limpar();
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
    // DEFINIÇÕES DOS STEPS (GIVEN/AND) - CheckIn Específicos
    // =======================================================

    // PASSO "que sou um usuário autenticado" REMOVIDO: ESTÁ EM HabitoSteps.java

    // PASSO "existe um hábito chamado {string} cadastrado" REMOVIDO: ESTÁ IMPLÍCITO NO HabitoSteps.java (se o passo for "E")

    @And("que o dia atual é {string}")
    public void que_o_dia_atual_e(String dataStr) {
        // Nada de código (apenas setup de cenário/variável)
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

        // Divide por vírgula
        Stream.of(diasStr.split(","))
                .map(String::trim)       // remove espaços antes/depois de cada data
                .forEach(dataStr -> {
                    LocalDate data = parseData(dataStr);  // agora dataStr = "01/11/2025", etc
                    try {
                        checkInService.marcarCheckIn(habitoSteps.usuario.getId(), habitoId, data);
                    } catch (Exception e) {
                        // ignora duplicidade
                    }
                });
    }




    // =======================================================
    // DEFINIÇÕES DOS STEPS (WHEN) - CheckIn Específicos
    // =======================================================

    @When("eu clico no botão {string} para o hábito {string} no dia {string}")
    public void eu_clico_no_botao_para_o_habito(String acao, String nomeHabito, String dataStr) {
        UUID habitoId = buscarHabitoIdPorNome(nomeHabito);
        LocalDate data = parseData(dataStr);
        UUID usuarioId = habitoSteps.usuario.getId();

        try {
            if ("Marcar como feito".equals(acao)) {
                checkInService.marcarCheckIn(usuarioId, habitoId, data);
            } else if ("Desmarcar check-in".equals(acao)) {
                checkInService.desmarcarCheckIn(usuarioId, habitoId, data);
            }
        } catch (Throwable e) {
            excecaoCapturada = e;
        }
    }

    @When("eu acesso a lista de check-ins para o hábito {string} no período de {string} a {string}")
    public void eu_acesso_a_lista_de_check_ins_para_o_habito_no_periodo(String nomeHabito, String dataInicialStr, String dataFinalStr) {
        UUID habitoId = buscarHabitoIdPorNome(nomeHabito);

        try {
            // 1. Lista todos os check-ins do hábito (sem filtro de data)
            checkinsListados = checkInService.listarCheckInsPorHabito(habitoSteps.usuario.getId(), habitoId);

            // 2. Simula a filtragem pelo período de calendário
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
    // VERIFICAÇÕES (THEN/AND) - CheckIn Específicos
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


    // PASSO "eu devo receber um erro informando que {string}" REMOVIDO: ESTÁ EM HabitoSteps.java
}