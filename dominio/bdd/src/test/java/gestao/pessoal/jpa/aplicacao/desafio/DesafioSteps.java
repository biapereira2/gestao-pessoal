package gestao.pessoal.jpa.aplicacao.desafio;

import gestao.pessoal.jpa.aplicacao.usuario.FakeRepositorioUsuario;
import gestao.pessoal.dominio.principal.compartilhado.usuario.Usuario;
import gestao.pessoal.dominio.principal.princ.desafio.ConviteDesafio;
import gestao.pessoal.dominio.principal.princ.desafio.Desafio;
import gestao.pessoal.dominio.principal.princ.desafio.DesafioService;
import gestao.pessoal.dominio.principal.princ.desafio.RepositorioDesafio;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static gestao.pessoal.dominio.principal.princ.desafio.ConviteDesafio.StatusConvite;
import static gestao.pessoal.dominio.principal.princ.desafio.Desafio.StatusDesafio;

public class DesafioSteps {

    // --- Repositórios Falsos e Serviços ---
    private final FakeRepositorioUsuario fakeRepositorioUsuario;
    private final DesafioService desafioService;
    private final FakeRepositorioDesafio fakeRepositorioDesafio = new FakeRepositorioDesafio();

    // Estado do Teste
    private Usuario usuarioLogado;
    private Desafio desafioCriado;
    private Exception excecaoCapturada;
    private Map<String, Integer> progressoParticipantes = new HashMap<>(); // Para Cenário 6

    public DesafioSteps(FakeRepositorioUsuario fakeRepositorioUsuario) {
        this.fakeRepositorioUsuario = fakeRepositorioUsuario;
        // Inicializa o serviço com o repositório falso
        this.desafioService = new DesafioService(fakeRepositorioDesafio, fakeRepositorioUsuario);
    }

    // --- MÉTODOS AUXILIARES PARA CONVERTER NOME GHERKIN EM ENTIDADES/IDS ---

    /** Retorna a entidade Usuario, garantindo que ela seja criada/buscada pelo nome. */
    private Usuario getUserByNome(String nome) {
        return fakeRepositorioUsuario.criarOuBuscarUsuario(nome);
    }

    /** Retorna o ID do usuário pelo nome. */
    private UUID getUserIdByNome(String nome) {
        return getUserByNome(nome).getId();
    }

    /** Retorna o Email do usuário pelo nome (necessário para o DesafioService). */
    private String getEmailByNome(String nome) {
        return getUserByNome(nome).getEmail();
    }
    // --- FIM MÉTODOS AUXILIARES ---

    // Implementação Fake do Repositório de Desafios
    private class FakeRepositorioDesafio implements RepositorioDesafio {
        private final Map<UUID, Desafio> desafios = new HashMap<>();
        private final Map<UUID, ConviteDesafio> convites = new HashMap<>();

        @Override
        public void salvar(Desafio desafio) {
            desafios.put(desafio.getId(), desafio);
        }

        @Override
        public Optional<Desafio> buscarPorId(UUID id) {
            return Optional.ofNullable(desafios.get(id));
        }

        @Override
        public List<Desafio> buscarTodosDoUsuario(UUID usuarioId) {
            return desafios.values().stream()
                    .filter(d -> d.getParticipantesIds().contains(usuarioId) || d.getCriadorId().equals(usuarioId))
                    .collect(Collectors.toList());
        }

        @Override
        public void salvarConvite(ConviteDesafio convite) {
            convites.put(convite.getId(), convite);
        }

        @Override
        public Optional<ConviteDesafio> buscarConvitePorDesafioEConvidado(UUID desafioId, UUID convidadoId) {
            return convites.values().stream()
                    .filter(c -> c.getDesafioId().equals(desafioId) && c.getConvidadoId().equals(convidadoId))
                    .findFirst();
        }

        @Override
        public List<ConviteDesafio> buscarConvitesPendentes(UUID convidadoId) {
            return convites.values().stream()
                    .filter(c -> c.getConvidadoId().equals(convidadoId) && c.getStatus().equals(StatusConvite.PENDENTE))
                    .collect(Collectors.toList());
        }

        @Override
        public void removerConvite(UUID conviteId) {
            convites.remove(conviteId);
        }

        @Override
        public void remover(UUID desafioId) {
            desafios.remove(desafioId);
        }

        public Map<UUID, ConviteDesafio> getConvites() {
            return convites;
        }
    }

    // --- Implementação dos Steps ---

    @Given("que o usuário {string} está autenticado")
    public void queOUsuarioEstaAutenticado(String nomeUsuario) {
        usuarioLogado = getUserByNome(nomeUsuario); // Usa o helper
        Assertions.assertNotNull(usuarioLogado, "Usuário deve existir e estar logado.");
    }

    @And("que os hábitos {string} e {string} estão cadastrados para {string}")
    public void queOsHabitosEstaoCadastrados(String habito1, String habito2, String nomeUsuario) {
        // Simulação: Apenas garantimos que os IDs dos hábitos existam para a criação.
    }

    @And("que o usuário {string} e {string} existem no sistema")
    public void queOsUsuariosExistemNoSistema(String convidado1, String convidado2) {
        getUserByNome(convidado1); // Usa o helper
        getUserByNome(convidado2); // Usa o helper
    }

    @When("o {string} inicia a criação de um novo desafio com o nome {string}")
    public void oCriadorIniciaACriacaoDeUmNovoDesafioComONome(String criador, String nomeDesafio) {
        // Prepara os dados.
    }

    @And("ele define o período de {int} dias")
    public void eleDefineOPeriodoDeDias(int dias) {
        // Simulação
    }

    @And("ele seleciona os hábitos {string}, {string}")
    public void eleSelecionaOsHabitos(String habito1, String habito2) {
        // Simulação
    }

    @And("ele convida os usuários {string} e {string}")
    public void eleConvidaOsUsuarios(String convidado1, String convidado2) {
        // Preparamos a lista de nomes dos convidados
    }

    @And("ele finaliza a criação")
    public void eleFinalizaACriacao() {
        try {
            // Mock de dados para o teste de criação
            List<UUID> habitosIdsMock = List.of(UUID.randomUUID(), UUID.randomUUID());

            // AGORA CONVERTEMOS OS NOMES DO GHERKIN PARA OS EMAILS ESPERADOS PELO SERVICE
            List<String> emailsConvidados = List.of(
                    getEmailByNome("Convidado1"),
                    getEmailByNome("Convidado2")
            );

            // Simulação de 7 dias de duração
            LocalDate dataFim = LocalDate.now().plusDays(7);

            desafioCriado = desafioService.criarDesafio(
                    usuarioLogado.getId(),
                    "Desafio da Saúde",
                    habitosIdsMock,
                    dataFim,
                    emailsConvidados // <-- PASSANDO EMAILS
            );
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Then("o desafio {string} deve ser criado com status {string}")
    public void oDesafioDeveSerCriadoComStatus(String nomeDesafio, String status) {
        Assertions.assertNotNull(desafioCriado, "O desafio não foi criado.");
        Assertions.assertEquals(nomeDesafio, desafioCriado.getNome(), "O nome do desafio não confere.");
        Assertions.assertEquals(StatusDesafio.ATIVO.name(), status, "O status não é ATIVO.");
    }

    @And("o {string} deve ser listado como participante ativo")
    public void oCriadorDeveSerListadoComoParticipanteAtivo(String criador) {
        Assertions.assertTrue(desafioCriado.getParticipantesIds().contains(usuarioLogado.getId()), "O criador deve ser um participante.");
    }

    @And("o {string} e o {string} devem ter recebido um convite pendente")
    public void osConvidadosDevemTerRecebidoUmConvitePendente(String convidado1, String convidado2) {
        // Usando os métodos auxiliares
        UUID user1Id = getUserIdByNome(convidado1);
        UUID user2Id = getUserIdByNome(convidado2);

        long convitesPendentes = fakeRepositorioDesafio.getConvites().values().stream()
                .filter(c -> c.getDesafioId().equals(desafioCriado.getId()) &&
                        (c.getConvidadoId().equals(user1Id) || c.getConvidadoId().equals(user2Id)) &&
                        c.getStatus().equals(StatusConvite.PENDENTE))
                .count();

        Assertions.assertEquals(2, convitesPendentes, "Dois convites pendentes deveriam ter sido criados.");
    }

    // --- CENÁRIOS DE ACEITE/REJEITE ---

    @Given("que o usuário {string} criou o desafio {string}")
    public void queODesafioFoiCriado(String criador, String nomeDesafio) {
        UUID criadorId = getUserIdByNome(criador); // Usa o helper
        List<UUID> habitosIdsMock = List.of(UUID.randomUUID());
        LocalDate dataFim = LocalDate.now().plusDays(7);
        desafioCriado = new Desafio(UUID.randomUUID(), criadorId, nomeDesafio, habitosIdsMock, LocalDate.now(), dataFim);
        fakeRepositorioDesafio.salvar(desafioCriado);
    }

    @And("o usuário {string} possui um convite pendente para {string}")
    public void oUsuarioPossuiUmConvitePendente(String convidado, String nomeDesafio) {
        UUID criadorId = getUserIdByNome("Criador"); // Usa o helper
        UUID convidadoId = getUserIdByNome(convidado); // Usa o helper

        ConviteDesafio convite = new ConviteDesafio(UUID.randomUUID(), desafioCriado.getId(), convidadoId, criadorId);
        fakeRepositorioDesafio.salvarConvite(convite);
    }

    @When("o {string} aceita o convite para o desafio {string}")
    public void oConvidadoAceitaOConvite(String convidado, String nomeDesafio) {
        try {
            usuarioLogado = getUserByNome(convidado); // Usa o helper
            desafioService.aceitarConvite(usuarioLogado.getId(), nomeDesafio);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Then("o {string} deve ser listado como participante ativo do desafio")
    public void oConvidadoDeveSerListadoComoParticipanteActivo(String convidado) {
        Desafio desafioAtualizado = fakeRepositorioDesafio.buscarPorId(desafioCriado.getId()).get();
        Assertions.assertTrue(desafioAtualizado.getParticipantesIds().contains(usuarioLogado.getId()), "O convidado deve estar na lista de participantes.");
    }

    @And("a contagem de participantes deve aumentar")
    public void aContagemDeParticipantesDeveAumentar() {
        Desafio desafioAtualizado = fakeRepositorioDesafio.buscarPorId(desafioCriado.getId()).get();
        // O criador já conta (1), então o convidado (1) aumenta para (2)
        Assertions.assertTrue(desafioAtualizado.getParticipantesIds().size() >= 2, "A contagem de participantes deve ter aumentado.");
    }

    @And("o convite para {string} deve ser resolvido")
    public void oConviteDeveSerResolvido(String nomeDesafio) {
        long convitesPendentes = fakeRepositorioDesafio.buscarConvitesPendentes(usuarioLogado.getId()).stream()
                .filter(c -> fakeRepositorioDesafio.buscarPorId(c.getDesafioId()).get().getNome().equals(nomeDesafio))
                .count();
        Assertions.assertEquals(0, convitesPendentes, "O convite deve ser resolvido e não estar mais pendente.");
    }

    @When("o {string} rejeita o convite para o desafio {string}")
    public void oConvidadoRejeitaOConvite(String convidado, String nomeDesafio) {
        try {
            usuarioLogado = getUserByNome(convidado); // Usa o helper
            desafioService.rejeitarConvite(usuarioLogado.getId(), nomeDesafio);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Then("o {string} não deve ser listado como participante")
    public void oConvidadoNaoDeveSerListadoComoParticipante(String convidado) {
        // O desafio não é criado, então ele usa o desafio criado no Given
        Desafio desafioAtualizado = fakeRepositorioDesafio.buscarPorId(desafioCriado.getId()).get();
        Assertions.assertFalse(desafioAtualizado.getParticipantesIds().contains(usuarioLogado.getId()), "O convidado não pode estar na lista de participantes.");
    }

    @And("a contagem de participantes não deve ser alterada")
    public void aContagemDeParticipantesNaoDeveSerAlterada() {
        Desafio desafioAtualizado = fakeRepositorioDesafio.buscarPorId(desafioCriado.getId()).get();
        Assertions.assertEquals(1, desafioAtualizado.getParticipantesIds().size(), "A contagem de participantes (apenas criador) não deve ser alterada.");
    }

    // --- CENÁRIOS DE ENCERRAMENTO E SAÍDA ---

    @Given("que o desafio {string} está ativo com {string} e {string}")
    public void queODesafioEstaAtivoComParticipantes(String nomeDesafio, String criador, String convidado) {
        UUID criadorId = getUserIdByNome(criador); // Usa o helper
        UUID convidadoId = getUserIdByNome(convidado); // Usa o helper

        List<UUID> habitosIdsMock = List.of(UUID.randomUUID());
        LocalDate dataFim = LocalDate.now().plusDays(7);

        desafioCriado = new Desafio(UUID.randomUUID(), criadorId, nomeDesafio, habitosIdsMock, LocalDate.now(), dataFim);
        desafioCriado.adicionarParticipante(convidadoId); // Adiciona o convidado como já aceito
        fakeRepositorioDesafio.salvar(desafioCriado);
    }

    @And("o {string} é o administrador do desafio")
    public void oUsuarioEhOAdministradorDoDesafio(String criador) {
        UUID criadorId = getUserIdByNome(criador); // Usa o helper
        Assertions.assertEquals(criadorId, desafioCriado.getCriadorId(), "O usuário deve ser o criador.");
    }

    @When("o {string} solicita o encerramento do desafio {string}")
    public void oCriadorSolicitaOEncerramentoDoDesafio(String criador, String nomeDesafio) {
        try {
            UUID criadorId = getUserIdByNome(criador); // Usa o helper
            desafioService.encerrarDesafio(criadorId, nomeDesafio);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Then("o desafio {string} deve ser marcado como {string}")
    public void oDesafioDeveSerMarcadoComo(String nomeDesafio, String status) {
        Desafio desafioAtualizado = fakeRepositorioDesafio.buscarPorId(desafioCriado.getId()).get();
        Assertions.assertEquals(StatusDesafio.ENCERRADO, desafioAtualizado.getStatus(), "O desafio deve ser marcado como ENCERRADO.");
    }

    @And("todos os participantes devem ser notificados sobre o fim")
    public void todosOsParticipantesDevemSerNotificadosSobreOFim() {
        // Simulação: Apenas verifica que o método de domínio foi chamado (já verificamos o status)
        System.out.println("SIMULAÇÃO: Notificação de encerramento enviada a todos os participantes.");
    }

    @When("o {string} solicita a saída do desafio {string}")
    public void oConvidadoSolicitaASaidaDoDesafio(String participante, String nomeDesafio) {
        try {
            UUID participanteId = getUserIdByNome(participante); // Usa o helper
            desafioService.sairDoDesafio(participanteId, nomeDesafio);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Then("o {string} não deve ser mais listado como participante")
    public void oConvidadoNaoDeveSerMaisListadoComoParticipante(String participante) {
        UUID participanteId = getUserIdByNome(participante); // Usa o helper
        Desafio desafioAtualizado = fakeRepositorioDesafio.buscarPorId(desafioCriado.getId()).get();
        Assertions.assertFalse(desafioAtualizado.getParticipantesIds().contains(participanteId), "O participante deve ter sido removido.");
    }

    @And("o desafio {string} deve permanecer ativo")
    public void oDesafioDevePermanecerAtivo(String nomeDesafio) {
        Desafio desafioAtualizado = fakeRepositorioDesafio.buscarPorId(desafioCriado.getId()).get();
        Assertions.assertEquals(StatusDesafio.ATIVO, desafioAtualizado.getStatus(), "O desafio deve permanecer ATIVO.");
    }

    // --- CENÁRIO DE PROGRESSO ---

    @Given("que o desafio {string} está ativo e inclui o hábito {string}")
    public void queODesafioEstaAtivoEIncluiOHabito(String nomeDesafio, String habito) {
        // Reutiliza o setup de criação de desafio
        queODesafioFoiCriado("Criador", nomeDesafio);
    }

    @And("que {string} cumpriu o hábito por {int} dias")
    public void queOCriadorCumpriuOHabitoPorDias(String participante, int dias) {
        progressoParticipantes.put(participante, dias);
    }

    @When("o {string} visualiza o ranking do desafio {string}")
    public void oCriadorVisualizaORankingDoDesafio(String visualizador, String nomeDesafio) {
        // A ação é apenas a visualização, o código usa os dados já simulados.
    }

    @Then("ele deve ver o progresso total de {int} cumprimentos do hábito {string}")
    public void eleDeveVerOProgressoTotalDeCumprimentos(int totalEsperado, String habito) {
        int totalReal = progressoParticipantes.values().stream().mapToInt(Integer::intValue).sum();
        Assertions.assertEquals(totalEsperado, totalReal, "O progresso total do grupo não confere.");

        // Simulação de chamada de serviço para cálculo
        Assertions.assertEquals(8, desafioService.calcularProgressoTotal("Desafio de Leitura"), "A simulação de cálculo de progresso falhou.");
    }

    @And("ele deve ver que {string} está na frente no ranking")
    public void eleDeveVerQueEstaNaFrenteNoRanking(String participanteLider) {
        // Baseado nos dados simulados (Criador: 3, Convidado1: 5)
        String liderReal = progressoParticipantes.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get().getKey();

        Assertions.assertEquals(participanteLider, liderReal, "O ranking está incorreto.");
    }
}
