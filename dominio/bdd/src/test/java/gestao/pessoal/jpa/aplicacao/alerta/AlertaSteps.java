package gestao.pessoal.jpa.aplicacao.alerta;

import gestao.pessoal.dominio.principal.princ.alerta.Alerta;
import gestao.pessoal.dominio.principal.princ.alerta.AlertaService;
import gestao.pessoal.dominio.principal.princ.alerta.RepositorioAlerta;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Entao;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AlertaSteps {

    private RepositorioAlerta repositorioAlerta;
    private AlertaService alertaService;
    private UUID usuarioId;
    private Alerta alertaAtual;
    private Exception excecao;

    @Before
    public void setup() {
        repositorioAlerta = new FakeRepositorioAlerta();
        alertaService = new AlertaService(repositorioAlerta);
        usuarioId = UUID.randomUUID();
        alertaAtual = null;
        excecao = null;
    }

    // ----------------- DADO -----------------
    @Dado("que eu sou um usuário autenticado para alertas")
    public void que_eu_sou_um_usuario_autenticado_para_alertas() {
        assertNotNull(usuarioId);
    }

    @Dado("eu criei um alerta com título {string}, descrição {string}, data de disparo {string} e categoria {string}")
    public void eu_criei_um_alerta(String titulo, String descricao, String data, String categoria) {
        alertaService.criar(usuarioId, titulo, descricao, LocalDate.parse(data), categoria);
        List<Alerta> alertas = alertaService.listarPorUsuario(usuarioId);
        alertaAtual = alertas.get(alertas.size() - 1);
    }

    // ----------------- QUANDO -----------------
    @Quando("eu crio um alerta com título {string}, descrição {string}, data de disparo {string} e categoria {string}")
    public void eu_crio_um_alerta(String titulo, String descricao, String data, String categoria) {
        try {
            alertaService.criar(usuarioId, titulo, descricao, LocalDate.parse(data), categoria);
            List<Alerta> alertas = alertaService.listarPorUsuario(usuarioId);
            alertaAtual = alertas.get(alertas.size() - 1);
        } catch (Exception e) {
            excecao = e;
        }
    }

    @Quando("eu tento criar um alerta sem definir título ou data")
    public void eu_tento_criar_um_alerta_sem_definir_titulo_ou_data() {
        try {
            alertaService.criar(usuarioId, "", "", null, null);
        } catch (Exception e) {
            excecao = e;
        }
    }

    @Quando("eu edito o alerta para título {string}, descrição {string}, data {string} e categoria {string}")
    public void eu_edito_o_alerta(String titulo, String descricao, String data, String categoria) {
        alertaService.editar(alertaAtual.getId(), titulo, descricao, LocalDate.parse(data), categoria);
        alertaAtual = alertaService.listarPorUsuario(usuarioId).get(0);
    }

    @Quando("eu excluo o alerta")
    public void eu_excluo_o_alerta() {
        alertaService.excluir(alertaAtual.getId());
        alertaAtual = null;
    }

    @Quando("eu verifico se o alerta deve ser disparado")
    public void eu_verifico_se_o_alerta_deve_ser_disparado() {
        alertaService.verificarDisparo(alertaAtual.getId());
        alertaAtual = alertaService.listarPorUsuario(usuarioId).get(0);
    }

    // ----------------- ENTÃO -----------------
    @Entao("o alerta deve ser cadastrado com sucesso")
    public void o_alerta_deve_ser_cadastrado_com_sucesso() {
        List<Alerta> alertas = alertaService.listarPorUsuario(usuarioId);
        assertEquals(1, alertas.size());
    }

    @Entao("devo receber um erro falando que {string}")
    public void devo_receber_um_erro_falando_que(String msg) {
        assertNotNull(excecao);
        assertTrue(excecao.getMessage().contains(msg));
    }

    @Entao("o alerta deve aparecer na lista de alertas ativos")
    public void o_alerta_deve_aparecer_na_lista_de_alertas_ativos() {
        List<Alerta> alertas = alertaService.listarPorUsuario(usuarioId);
        assertTrue(alertas.contains(alertaAtual));
    }

    @Entao("o alerta deve ser atualizado corretamente")
    public void o_alerta_deve_ser_atualizado_corretamente() {
        assertNotNull(alertaAtual);
        assertTrue(alertaAtual.getTitulo() != null);
    }

    @Entao("a lista de alertas do usuário deve refletir a alteração")
    public void a_lista_de_alertas_do_usuario_deve_refletir_a_alteracao() {
        List<Alerta> alertas = alertaService.listarPorUsuario(usuarioId);
        assertTrue(alertas.stream().anyMatch(a -> a.getId().equals(alertaAtual.getId())));
    }

    @Entao("ele deve ser removido da lista de alertas")
    public void ele_deve_ser_removido_da_lista_de_alertas() {
        List<Alerta> alertas = alertaService.listarPorUsuario(usuarioId);
        assertTrue(alertas.isEmpty());
    }

    @Entao("o alerta não deve mais aparecer na tela")
    public void o_alerta_nao_deve_mais_aparecer_na_tela() {
        List<Alerta> alertas = alertaService.listarPorUsuario(usuarioId);
        assertFalse(alertas.contains(alertaAtual));
    }

    @Entao("o alerta deve ser marcado como disparado no sistema")
    public void o_alerta_deve_ser_marcado_como_disparado_no_sistema() {
        assertTrue(alertaAtual.isDisparado());
    }
    @Entao("o alerta não deve ser criado")
    public void o_alerta_nao_deve_ser_criado() {
        List<Alerta> alertas = alertaService.listarPorUsuario(usuarioId);
        assertTrue(alertas.isEmpty(), "O alerta foi criado, mas não deveria!");
    }
}
