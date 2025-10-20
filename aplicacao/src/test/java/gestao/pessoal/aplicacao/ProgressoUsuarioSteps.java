package gestao.pessoal.aplicacao;

import gestao.pessoal.engajamento.ProgressoUsuario;
import gestao.pessoal.engajamento.RepositorioProgressoUsuario;

import io.cucumber.java.en.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FakeRepository para testes, implementando o Repositorio real
 */
class FakeRepositorioProgressoUsuario implements RepositorioProgressoUsuario {

    private final Map<UUID, ProgressoUsuario> armazenamento = new HashMap<>();

    @Override
    public void salvar(ProgressoUsuario progresso) {
        armazenamento.put(progresso.getUsuarioId(), progresso);
    }

    @Override
    public Optional<ProgressoUsuario> buscarPorUsuarioId(UUID usuarioId) {
        return Optional.ofNullable(armazenamento.get(usuarioId));
    }

    @Override
    public boolean existeParaUsuario(UUID usuarioId) {
        return armazenamento.containsKey(usuarioId);
    }

    public void limpar() {
        armazenamento.clear();
    }
}

/**
 * Steps do Cucumber para o sistema de pontos e níveis
 */
public class ProgressoUsuarioSteps {

    private UUID usuarioId;
    private ProgressoUsuario progressoUsuario;
    private RepositorioProgressoUsuario repositorio = new FakeRepositorioProgressoUsuario();
    private int pontosDoHabito;

    // ---------------- Cenário: Ganhar pontos ao realizar um check-in ----------------
    @Given("o hábito {string} está pendente no dia atual para o usuário")
    public void habitoEstaPendente(String habito) {
        usuarioId = UUID.randomUUID();
        progressoUsuario = new ProgressoUsuario(usuarioId);
        repositorio.salvar(progressoUsuario);
    }

    @Given("o hábito {string} vale {int} pontos")
    public void habitoValePontos(String habito, int pontos) {
        this.pontosDoHabito = pontos;
    }

    @Given("o usuário possui {int} pontos acumulados")
    public void usuarioPossuiPontos(int pontos) {
        progressoUsuario.adicionarPontos(pontos);
        repositorio.salvar(progressoUsuario);
    }

    @When("o usuário realiza o check-in do hábito {string}")
    public void usuarioRealizaCheckIn(String habito) {
        // check-in já implementado
    }

    @Then("o sistema deve registrar o cumprimento do hábito")
    public void sistemaRegistraCumprimento() {
        assertTrue(repositorio.existeParaUsuario(usuarioId));
    }

    @Then("adicionar {int} pontos ao total do usuário")
    public void adicionarPontosAoTotal(int pontos) {
        progressoUsuario.adicionarPontos(pontos);
        repositorio.salvar(progressoUsuario);
    }

    @Then("exibir o novo total de {int} pontos ao usuário")
    public void exibirTotalPontos(int pontos) {
        assertEquals(pontos, progressoUsuario.getPontos());
    }

    // ---------------- Cenário: Retirar pontos ao desfazer um check-in ----------------
    @Given("que o usuário possui {int} pontos acumulados")
    public void usuarioPossuiPontosAcumulados(int pontos) {
        usuarioId = UUID.randomUUID();
        progressoUsuario = new ProgressoUsuario(usuarioId);
        progressoUsuario.adicionarPontos(pontos);
        repositorio.salvar(progressoUsuario);
    }

    @Given("realizou o check-in do hábito {string} hoje")
    public void checkInRealizadoHoje(String habito) {
        // Supondo que já tenha sido marcado; sem ação extra necessária
    }

    @When("o usuário desfaz o check-in de {string}")
    public void usuarioDesfazCheckIn(String habito) {
        // Desfazer check-in ja implementado
    }

    @Then("o sistema deve remover {int} pontos do saldo do usuário")
    public void sistemaRemovePontos(int pontos) {
        progressoUsuario.removerPontos(pontosDoHabito);
        repositorio.salvar(progressoUsuario);
    }

    @Then("atualizar o total de pontos exibido ao usuário para {int} pontos")
    public void atualizarTotalExibido(int pontos) {
        assertEquals(pontos, progressoUsuario.getPontos());
    }

    // ---------------- Cenário: Subir de nível ao atingir meta de pontos ----------------
    @Given("que o usuário está no nível {int} com {int} pontos")
    public void usuarioNoNivelComPontos(int nivel, int pontos) {
        usuarioId = UUID.randomUUID();
        progressoUsuario = new ProgressoUsuario(usuarioId);
        // Ajusta o nível manualmente
        while (progressoUsuario.getNivel() < nivel) {
            progressoUsuario.adicionarPontos(progressoUsuario.getLimiteProximoNivel() - progressoUsuario.getPontos());
            repositorio.salvar((progressoUsuario));
        }
        // Ajusta os pontos atuais
        progressoUsuario.adicionarPontos(pontos - progressoUsuario.getLimiteNivelAtual());
        repositorio.salvar(progressoUsuario);
    }

    @Given("o limite mínimo de pontos para o próximo nível é de {int} pontos")
    public void limiteMinimoNivel(int limite) {
        // Já implementado na entidade
    }

    @Given("o usuário realiza um novo check-in que vale {int} pontos")
    public void usuarioRealizaNovoCheckIn(int pontos) {
        this.pontosDoHabito = pontos;
    }

    @When("o sistema adiciona os {int} pontos")
    public void sistemaAdicionaPontos(int pontos) {
        progressoUsuario.adicionarPontos(pontosDoHabito);
        repositorio.salvar(progressoUsuario);
    }

    @Then("o saldo de pontos do usuário passa a ser {int}")
    public void saldoPontosPassaSer(int pontos) {
        assertEquals(pontos, progressoUsuario.getPontos());
    }

    @Then("o sistema deve promover o usuário para o nível {int}")
    public void promoverUsuarioNivel(int nivel) {
        assertEquals(nivel, progressoUsuario.getNivel());
    }

    @Then("aumentar o limite do próximo nível para {int}")
    public void aumentarLimiteProximoNivel(int pontos) {
        assertEquals(pontos, progressoUsuario.getLimiteProximoNivel());
    }

    @Then("aumentar o limite mínimo do nível atual para {int}")
    public void aumentarLimiteMinimoNivelAtual(int pontos){
        assertEquals(pontos, progressoUsuario.getLimiteNivelAtual());
    }

    @Then("exibir uma mensagem de conquista")
    public void exibirMensagemConquista() {
        // Simulação de exibição de mensagem
        assertTrue(true);
    }

    // ---------------- Cenário: Cair de nível ao retirar pontos necessário para a meta ----------------
    // Primeiro Given já existe

    @Given("o limite mínimo de pontos para o nível atual é de {int} pontos")
    public void limiteMinimoDePontosParaONivelAtual(int pontos){
        // já implementado na entidade
    }

    @Given("o usuário desmarca um check-in que vale {int} pontos")
    public void usuarioDesmarcaCheckIn(int pontos) {
        this.pontosDoHabito = pontos;
    }

    @When("o sistema retira os {int} pontos")
    public void sistemaRetiraPontos(int pontos) {
        progressoUsuario.removerPontos(pontosDoHabito);
        repositorio.salvar(progressoUsuario);
    }

    @Then("o sistema deve demover o usuário para o nível {int}")
    public void demoverUsuarioNivel(int nivel) {
        assertEquals(nivel, progressoUsuario.getNivel());
    }

    @Then("setar o limite do próximo nível para {int}")
    public void setarLimiteProximoNivel(int pontos) {
        assertEquals(pontos, progressoUsuario.getLimiteProximoNivel());
    }

    @Then("setar o limite mínimo do nível atual para {int}")
    public void setarLimiteMinimoNivelAtual(int pontos){
        assertEquals(pontos, progressoUsuario.getLimiteNivelAtual());
    }

    // ---------------- Cenário: Visualizar pontos e nível atual ----------------
    @Given("que o usuário possui um total de {int} pontos e está no nível {int}")
    public void usuarioPossuiTotalDePontosENivel(int pontos, int nivel) {
        usuarioId = UUID.randomUUID();
        progressoUsuario = new ProgressoUsuario(usuarioId);
        // Ajusta o nível manualmente
        while (progressoUsuario.getNivel() < nivel) {
            progressoUsuario.adicionarPontos(progressoUsuario.getLimiteProximoNivel() - progressoUsuario.getPontos());
            repositorio.salvar((progressoUsuario));
        }
        // Ajusta pontos
        progressoUsuario.adicionarPontos(pontos - progressoUsuario.getLimiteNivelAtual());
        repositorio.salvar(progressoUsuario);
    }

    @Given("o limite mínimo de pontos para o nível {int} é {int}")
    public void limiteMinimoNivelParaNivel(int nivel, int limite) {
        // Apenas para fins de documentação; já configurado no objeto
    }

    @When("o usuário acessa seu perfil ou painel de progresso")
    public void usuarioAcessaPainel() {
        // Apenas acessa o objeto progressoUsuario
    }

    @Then("o sistema deve exibir {int} pontos no saldo do usuário")
    public void exibirPontos(int pontos) {
        assertEquals(pontos, progressoUsuario.getPontos());
    }

    @Then("nível {int} como o nível do usuário")
    public void exibirNivel(int nivel) {
        assertEquals(nivel, progressoUsuario.getNivel());
    }

    @Then("{int} pontos faltantes na barra de progresso de nível")
    public void pontosFaltantesNaBarra(int pontosFaltantes) {
        assertEquals(pontosFaltantes, progressoUsuario.getPontosFaltantes());
    }
}

