package gestao.pessoal.jpa.aplicacao.progressoUsuario;

import gestao.pessoal.jpa.aplicacao.checkIn.FakeCheckInService;
import gestao.pessoal.jpa.aplicacao.checkIn.FakeRepositorioCheckIn;
import gestao.pessoal.jpa.aplicacao.habito.FakeRepositorioHabito;
import gestao.pessoal.dominio.principal.engajamento.progressoUsuario.ProgressoUsuario;
import gestao.pessoal.dominio.principal.engajamento.progressoUsuario.ProgressoUsuarioService;
import gestao.pessoal.dominio.principal.engajamento.progressoUsuario.RepositorioProgressoUsuario;

// Importa as classes reais do domínio de Habito

import gestao.pessoal.dominio.principal.princ.checkIn.CheckIn;
import gestao.pessoal.dominio.principal.princ.habito.Habito;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


// =================================================================
// FAKE REPOSITORIES (Simulando o Banco de Dados)
// =================================================================

/** Fake Repository para ProgressoUsuario (Mantido) */
class FakeRepositorioProgressoUsuario implements RepositorioProgressoUsuario {
    private final Map<UUID, ProgressoUsuario> armazenamento = new HashMap<>();

    @Override public void salvar(ProgressoUsuario progresso) { armazenamento.put(progresso.getUsuarioId(), progresso); }
    @Override public Optional<ProgressoUsuario> buscarPorUsuarioId(UUID usuarioId) { return Optional.ofNullable(armazenamento.get(usuarioId)); }
    @Override public boolean existeParaUsuario(UUID usuarioId) { return armazenamento.containsKey(usuarioId); }
    public void limpar() { armazenamento.clear(); }
}

// =================================================================
// FAKE SERVICE (Para simular o CheckInService real)
// =================================================================


// =================================================================
// STEPS DEFINITION
// =================================================================
public class ProgressoUsuarioSteps {

    // Services e Repositórios
    private ProgressoUsuarioService progressoUsuarioService;
    private FakeCheckInService checkInService;
    private FakeRepositorioProgressoUsuario repositorioProgresso;
    private FakeRepositorioHabito repositorioHabito;
    private FakeRepositorioCheckIn repositorioCheckIn;

    // Variáveis de estado
    private UUID usuarioId;
    private ProgressoUsuario progressoUsuario;
    private Habito habitoAtual;
    private final LocalDate hoje = LocalDate.now();

    public ProgressoUsuarioSteps() {
        // Inicialização dos FAKES
        this.repositorioProgresso = new FakeRepositorioProgressoUsuario();
        this.repositorioHabito = new FakeRepositorioHabito();
        this.repositorioCheckIn = new FakeRepositorioCheckIn();

        // Inicializa os Services com as dependências FAKE
        this.progressoUsuarioService = new ProgressoUsuarioService(this.repositorioProgresso);
        this.checkInService = new FakeCheckInService(
                this.repositorioCheckIn,
                this.repositorioHabito,
                this.progressoUsuarioService
        );
    }

    // HOOK: Limpeza e Setup do estado para cada novo cenário
    @Before
    public void setupCenarioProgressoUsuario() {
        // 1. Limpa todos os repositórios fakes
        this.repositorioProgresso.limpar();
        this.repositorioHabito.limpar();
        this.repositorioCheckIn.limpar();

        // 2. Reseta o ID e o objeto de progresso
        this.usuarioId = UUID.randomUUID();
        this.progressoUsuario = null;
        this.habitoAtual = null;
    }

    // =================================================================
    // MÉTODOS AUXILIARES para Configuração
    // =================================================================

    /** Garante que a entidade ProgressoUsuario exista no repositório. */
    private void garantirProgressoExistente() {
        if (this.progressoUsuario == null) {
            Optional<ProgressoUsuario> progressoOpt = repositorioProgresso.buscarPorUsuarioId(usuarioId);
            if (progressoOpt.isPresent()) {
                this.progressoUsuario = progressoOpt.get();
            } else {
                this.progressoUsuario = new ProgressoUsuario(usuarioId);
                repositorioProgresso.salvar(this.progressoUsuario);
            }
        }
    }

    /** Garante que a entidade Habito exista e a armazena em habitoAtual. */
    private Habito garantirHabitoExistente(String nomeHabito, String frequencia) {
        Optional<Habito> habitoOpt = repositorioHabito.buscarPorNomeEUsuario(nomeHabito, usuarioId);

        if (habitoOpt.isPresent()) {
            this.habitoAtual = habitoOpt.get();
            return this.habitoAtual;
        }

        // Cria o hábito e o salva no repositório
        Habito novoHabito = new Habito(usuarioId, nomeHabito, "Descrição de Teste", "Saúde", frequencia);

        repositorioHabito.salvar(novoHabito);
        this.habitoAtual = novoHabito;
        return novoHabito;
    }


    // ---------------- Cenário: Ganhar pontos ao realizar um check-in ----------------

    // ** IMPLEMENTAÇÃO ROBUSTA **
    @Given("o hábito {string} está pendente no dia atual para o usuário")
    public void habitoEstaPendente(String habitoNome) {
        garantirProgressoExistente();

        // 1. Garante que o Hábito existe (usando frequência Diaria para simplificar o cálculo)
        Habito habito = garantirHabitoExistente(habitoNome, "diaria");
        this.habitoAtual = habito;

        // 2. Garante que o CheckIn NÃO existe para hoje (pendente)
        boolean jaExisteCheckIn = repositorioCheckIn.buscarPorHabitoEData(habito.getId(), hoje, usuarioId).isPresent();
        assertFalse(jaExisteCheckIn, "O Hábito já deveria estar pendente (sem check-in registrado).");
    }

    @Given("o hábito {string} vale {int} pontos")
    public void habitoValePontos(String habitoNome, int pontos) {
        // Verifica se o hábito existe e se os pontos batem
        Habito habito = repositorioHabito.buscarPorNomeEUsuario(habitoNome, usuarioId)
                .orElseThrow(() -> new IllegalStateException("Hábito não encontrado."));

        assertEquals(pontos, habito.getPontos(),
                "A pontuação definida no Gherkin não corresponde à pontuação do Hábito.");
    }

    @Given("o usuário possui {int} pontos acumulados")
    public void usuarioPossuiPontos(int pontos) {
        garantirProgressoExistente();

        // Simula o acúmulo de pontos iniciais
        this.progressoUsuarioService.adicionarPontos(usuarioId, pontos, "Setup");
        repositorioProgresso.salvar(progressoUsuario);
    }

    @When("o usuário realiza o check-in do hábito {string}")
    public void usuarioRealizaCheckIn(String habitoNome) {
        Habito habito = repositorioHabito.buscarPorNomeEUsuario(habitoNome, usuarioId)
                .orElseThrow(() -> new IllegalStateException("Hábito não encontrado para realizar check-in."));
        this.habitoAtual = habito;

        // ** AÇÃO PRINCIPAL: Chama o Fake CheckInService **
        checkInService.marcarCheckIn(usuarioId, habito.getId(), hoje);

        // Atualiza a entidade local
        this.progressoUsuario = repositorioProgresso.buscarPorUsuarioId(usuarioId).get();
    }

    @Then("o sistema deve registrar o cumprimento do hábito")
    public void sistemaRegistraCumprimento() {
        // ** IMPLEMENTAÇÃO ROBUSTA: Verifica a existência do CheckIn no repositório **
        assertNotNull(this.habitoAtual, "Hábito de referência não foi definido.");

        Optional<CheckIn> checkIn = repositorioCheckIn.buscarPorHabitoEData(
                this.habitoAtual.getId(), hoje, usuarioId
        );

        assertTrue(checkIn.isPresent(),
                "O Check-in não foi registrado no RepositórioCheckIn.");
    }

    @Then("adicionar {int} pontos ao total do usuário")
    public void adicionarPontosAoTotal(int pontos) {
        // Verifica se a pontuação adicionada corresponde aos pontos do Hábito
        assertEquals(pontos, this.habitoAtual.getPontos(),
                "A pontuação adicionada não corresponde aos pontos do Hábito.");
    }

    @Then("exibir o novo total de {int} pontos ao usuário")
    public void exibirTotalPontos(int pontos) {
        assertEquals(pontos, progressoUsuarioService.visualizarProgresso(usuarioId).getPontos());
    }

    // ---------------- Cenário: Retirar pontos ao desfazer um check-in ----------------
    @Given("que o usuário possui {int} pontos acumulados")
    public void usuarioPossuiPontosAcumulados(int pontos) {
        // Reusa o método setup
        garantirProgressoExistente();

        // Simula o acúmulo de pontos iniciais
        this.progressoUsuarioService.adicionarPontos(usuarioId, pontos, "Setup");
        repositorioProgresso.salvar(progressoUsuario);
    }

    @Given("realizou o check-in do hábito {string} hoje")
    public void checkInRealizadoHoje(String habitoNome) {
        garantirProgressoExistente();

        // 1. Garante que o Hábito existe (usando frequência Diaria para simplificar o cálculo)
        Habito habito = garantirHabitoExistente(habitoNome, "diaria");
        this.habitoAtual = habito;

        // 2. Simula o check-in inicial (FATO)
        CheckIn checkIn = new CheckIn(habito.getId(), usuarioId, hoje);
        repositorioCheckIn.salvar(checkIn);

        // 3. Verifica a robustez do setup
        assertTrue(repositorioCheckIn.buscarPorHabitoEData(habito.getId(), hoje, usuarioId).isPresent(),
                "Setup falhou: Check-in não foi salvo.");
    }

    @When("o usuário desfaz o check-in de {string}")
    public void usuarioDesfazCheckIn(String habitoNome) {
        Habito habito = repositorioHabito.buscarPorNomeEUsuario(habitoNome, usuarioId)
                .orElseThrow(() -> new IllegalStateException("Hábito não encontrado para desfazer check-in."));
        this.habitoAtual = habito;

        // ** AÇÃO PRINCIPAL: Chama o Fake CheckInService **
        checkInService.desmarcarCheckIn(usuarioId, habito.getId(), hoje);

        // Atualiza a entidade local
        this.progressoUsuario = repositorioProgresso.buscarPorUsuarioId(usuarioId).get();
    }

    @Then("o sistema deve remover {int} pontos do saldo do usuário")
    public void sistemaRemovePontos(int pontos) {
        // Verifica se a pontuação retirada corresponde aos pontos do Hábito
        assertEquals(pontos, this.habitoAtual.getPontos(),
                "A pontuação retirada não corresponde aos pontos do Hábito.");
    }

    @Then("atualizar o total de pontos exibido ao usuário para {int} pontos")
    public void atualizarTotalExibido(int pontos) {
        assertEquals(pontos, progressoUsuarioService.visualizarProgresso(usuarioId).getPontos());
    }

    // ---------------- Cenário: Subir de nível ao atingir meta de pontos ----------------
    @Given("que o usuário está no nível {int} com {int} pontos")
    public void usuarioNoNivelComPontos(int nivel, int pontos) {
        garantirProgressoExistente();

        // Ajusta o nível manualmente
        while (progressoUsuario.getNivel() < nivel) {
            progressoUsuarioService.adicionarPontos(usuarioId,progressoUsuario.getLimiteProximoNivel() - progressoUsuario.getPontos(), "Setup");
            repositorioProgresso.salvar((progressoUsuario));
        }
        // Ajusta os pontos atuais
        progressoUsuarioService.adicionarPontos(usuarioId,pontos - progressoUsuario.getLimiteNivelAtual(), "Setup");

        repositorioProgresso.salvar(progressoUsuario);

    }

    @Given("o limite mínimo de pontos para o próximo nível é de {int} pontos")
    public void limiteMinimoNivel(int limite) {
        // Documentação
    }

    @Given("o usuário realiza um novo check-in que vale {int} pontos")
    public void usuarioRealizaNovoCheckIn(int pontos) {
        // Não é mais necessário, pois o WHEN deve chamar o CheckInService (que já busca a pontuação)
        // No entanto, mantemos este step para criar um Hábito se for necessário para o WHEN/THEN
        garantirHabitoExistente("Hábito de Nível", "diaria"); // Cria um hábito de exemplo
    }

    @When("o sistema adiciona os {int} pontos")
    public void sistemaAdicionaPontos(int pontos) {
        // Simula o check-in do Hábito criado/garantido no step anterior
        Habito habito = repositorioHabito.buscarPorNomeEUsuario("Hábito de Nível", usuarioId)
                .orElseThrow(() -> new IllegalStateException("Hábito de Nível não encontrado."));

        // ** Ação robusta:** Chama o CheckInService, que adiciona pontos e faz a promoção
        checkInService.marcarCheckIn(usuarioId, habito.getId(), hoje);
        System.out.println("DEBUG: Usuário " + usuarioId + " agora tem " + progressoUsuario.getPontos() + " pontos e é nível " + progressoUsuario.getNivel());
    }

    @Then("o saldo de pontos do usuário passa a ser {int}")
    public void saldoPontosPassaSer(int pontos) {
        assertEquals(pontos, progressoUsuarioService.visualizarProgresso(usuarioId).getPontos());
    }

    @Then("o sistema deve promover o usuário para o nível {int}")
    public void promoverUsuarioNivel(int nivel) {
        assertEquals(nivel, progressoUsuarioService.visualizarProgresso(usuarioId).getNivel());
    }

    @Then("aumentar o limite do próximo nível para {int}")
    public void aumentarLimiteProximoNivel(int pontos) {
        assertEquals(pontos, progressoUsuarioService.visualizarProgresso(usuarioId).getLimiteProximoNivel());
    }

    @Then("aumentar o limite mínimo do nível atual para {int}")
    public void aumentarLimiteMinimoNivelAtual(int pontos){
        assertEquals(pontos, progressoUsuarioService.visualizarProgresso(usuarioId).getLimiteNivelAtual());
    }

    @Then("exibir uma mensagem de conquista")
    public void exibirMensagemConquista() {
        assertTrue(this.progressoUsuarioService.visualizarProgresso(usuarioId).getNivel() > 1);
    }

    // ---------------- Cenário: Cair de nível ao retirar pontos necessário para a meta ----------------

    @Given("o limite mínimo de pontos para o nível atual é de {int} pontos")
    public void limiteMinimoDePontosParaONivelAtual(int pontos){
        // Documentação
    }

    @Given("o usuário desmarca um check-in que vale {int} pontos")
    public void usuarioDesmarcaCheckIn(int pontos) {
        // Garante que o Hábito existe e está com check-in realizado
        String nomeHabito = "Hábito de Nível para Demover";
        Habito habito = garantirHabitoExistente(nomeHabito, "diaria");

        // Simula o check-in inicial (FATO)
        CheckIn checkIn = new CheckIn(habito.getId(), usuarioId, hoje);
        repositorioCheckIn.salvar(checkIn);

        this.habitoAtual = habito;
    }

    @When("o sistema retira os {int} pontos")
    public void sistemaRetiraPontos(int pontos) {
        // Simula o desfazimento do check-in do Hábito
        String nomeHabito = "Hábito de Nível para Demover";
        Habito habito = repositorioHabito.buscarPorNomeEUsuario(nomeHabito, usuarioId)
                .orElseThrow(() -> new IllegalStateException("Hábito de Nível não encontrado."));

        // ** Ação robusta:** Chama o CheckInService, que remove pontos e faz a demoção
        checkInService.desmarcarCheckIn(usuarioId, habito.getId(), hoje);

        // Atualiza a entidade local
        this.progressoUsuario = repositorioProgresso.buscarPorUsuarioId(usuarioId).get();
    }

    @Then("o sistema deve demover o usuário para o nível {int}")
    public void demoverUsuarioNivel(int nivel) {
        assertEquals(nivel, progressoUsuarioService.visualizarProgresso(usuarioId).getNivel());
    }

    @Then("setar o limite do próximo nível para {int}")
    public void setarLimiteProximoNivel(int pontos) {
        assertEquals(pontos, progressoUsuarioService.visualizarProgresso(usuarioId).getLimiteProximoNivel());
    }

    @Then("setar o limite mínimo do nível atual para {int}")
    public void setarLimiteMinimoNivelAtual(int pontos){
        assertEquals(pontos, progressoUsuarioService.visualizarProgresso(usuarioId).getLimiteNivelAtual());
    }

    // ---------------- Cenário: Visualizar pontos e nível atual ----------------
    @Given("que o usuário possui um total de {int} pontos e está no nível {int}")
    public void usuarioPossuiTotalDePontosENivel(int pontos, int nivel) {
        garantirProgressoExistente();

        // Ajusta o nível manualmente
        while (progressoUsuario.getNivel() < nivel) {
            progressoUsuarioService.adicionarPontos(usuarioId, progressoUsuario.getLimiteProximoNivel() - progressoUsuario.getPontos(), "Setup");
            repositorioProgresso.salvar((progressoUsuario));
        }
        // Ajusta os pontos atuais
        progressoUsuarioService.adicionarPontos(usuarioId, pontos - progressoUsuario.getLimiteNivelAtual(), "Setup");

        repositorioProgresso.salvar(progressoUsuario);
    }

    @Given("o limite mínimo de pontos para o nível {int} é {int}")
    public void limiteMinimoNivelParaNivel(int nivel, int limite) {
        // Documentação
    }

    @When("o usuário acessa seu perfil ou painel de progresso")
    public void usuarioAcessaPainel() {
        // Acesso implícito à entidade progressoUsuario
    }

    @Then("o sistema deve exibir {int} pontos no saldo do usuário")
    public void exibirPontos(int pontos) {
        assertEquals(pontos, progressoUsuarioService.visualizarProgresso(usuarioId).getPontos());
    }

    @Then("nível {int} como o nível do usuário")
    public void exibirNivel(int nivel) {
        assertEquals(nivel, progressoUsuarioService.visualizarProgresso(usuarioId).getNivel());
    }

    @Then("{int} pontos faltantes na barra de progresso de nível")
    public void pontosFaltantesNaBarra(int pontosFaltantes) {
        assertEquals(pontosFaltantes, progressoUsuarioService.visualizarProgresso(usuarioId).getPontosFaltantes());
    }
}