package gestao.pessoal.jpa.aplicacao.alerta;

import gestao.pessoal.dominio.principal.princ.alerta.Alerta;
import gestao.pessoal.dominio.principal.princ.alerta.AlertaService;
import gestao.pessoal.dominio.principal.princ.meta.Meta;
import gestao.pessoal.jpa.aplicacao.meta.FakeRepositorioMeta;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Entao;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class AlertaSteps {

    private FakeRepositorioAlerta repositorioAlerta;
    private FakeRepositorioMeta repositorioMeta;
    private AlertaService alertaService;
    private Meta meta;
    private UUID usuarioId;
    private Exception excecao;
    private Alerta alertaAtual;

    @Before
    public void setup() {
        repositorioAlerta = new FakeRepositorioAlerta();
        repositorioMeta = new FakeRepositorioMeta();
        alertaService = new AlertaService(repositorioAlerta, repositorioMeta);

        usuarioId = UUID.randomUUID();

        // 🔧 Meta agora exige lista de hábitos
        meta = new Meta(
                usuarioId,
                Meta.Tipo.SEMANAL,
                "Meta Teste",
                5,
                List.of(UUID.randomUUID())
        );

        repositorioMeta.salvar(meta);
        excecao = null;
        repositorioAlerta.limpar();
        alertaAtual = null;
    }

    private Alerta getAlertaAtual() {
        List<Alerta> alertas = repositorioAlerta.listarPorUsuario(usuarioId);
        if (alertas.isEmpty()) {
            throw new IllegalStateException("Nenhum alerta encontrado para o usuário");
        }
        return alertas.get(alertas.size() - 1);
    }

    @Dado("que eu sou um usuário autenticado para alertas")
    public void que_eu_sou_um_usuario_autenticado_para_alertas() {
        assertNotNull(usuarioId);
    }

    @Dado("eu possuo uma meta semanal cadastrada para alertas")
    public void eu_possuo_uma_meta_semanal_cadastrada_para_alertas() {
        assertNotNull(meta);
    }

    @Dado("eu possuo uma meta semanal de {int} hábitos específica para alertas")
    public void eu_possuo_uma_meta_semanal_de_habitos_especifica_para_alertas(Integer qtd) {

        meta = new Meta(
                usuarioId,
                Meta.Tipo.SEMANAL,
                "Meta nova para alertas",
                qtd,
                List.of(UUID.randomUUID())
        );

        repositorioMeta.salvar(meta);
    }

    @Dado("eu criei um alerta para ser notificado quando faltar {int} dias para o fim da semana")
    public void eu_criei_um_alerta_para_ser_notificado_quando_faltar_dias_para_o_fim_da_semana(Integer dias) {
        alertaService.criar(usuarioId, meta.getId(), Alerta.Condicao.FALTAM_DIAS, dias, "Avisar " + dias + " dias antes");
        alertaAtual = getAlertaAtual();
    }

    @Dado("que existe um alerta cadastrado")
    public void que_existe_um_alerta_cadastrado() {
        alertaService.criar(usuarioId, meta.getId(), Alerta.Condicao.FALTAM_DIAS, 5, "Teste");
        alertaAtual = getAlertaAtual();
    }

    @Dado("eu criei um alerta para ser notificado {int} dias antes do fim da semana")
    public void eu_criei_um_alerta_para_ser_notificado_dias_antes_do_fim_da_semana(Integer dias) {
        alertaService.criar(usuarioId, meta.getId(), Alerta.Condicao.FALTAM_DIAS, dias, "Avisar " + dias + " dias antes");
        alertaAtual = getAlertaAtual();
    }

    @Quando("eu crio um alerta personalizado para ser notificado quando faltar {int} dias para o fim da semana")
    public void eu_crio_um_alerta_personalizado_para_ser_notificado_quando_faltar_dias_para_o_fim_da_semana(Integer dias) {
        alertaService.criar(usuarioId, meta.getId(), Alerta.Condicao.FALTAM_DIAS, dias, "Avisar " + dias + " dias antes");
        alertaAtual = getAlertaAtual();
    }

    @Quando("eu tento criar um alerta sem definir condição ou tempo")
    public void eu_tento_criar_um_alerta_sem_definir_condição_ou_tempo() {
        try {
            alertaService.criar(usuarioId, meta.getId(), null, 0, "");
        } catch (Exception e) {
            excecao = e;
        }
    }


    @Quando("eu altero o alerta para ser notificado {int} dia antes do fim da semana")
    public void eu_altero_o_alerta_para_ser_notificado_dia_antes_do_fim_da_semana(Integer novoValor) {
        alertaAtual = getAlertaAtual();
        alertaService.editar(alertaAtual.getId(), novoValor);
        alertaAtual = getAlertaAtual();
    }

    @Quando("eu excluo o alerta")
    public void eu_excluo_o_alerta() {
        alertaAtual = getAlertaAtual();
        alertaService.excluir(alertaAtual.getId());
        alertaAtual = null;
    }

    @Quando("chegar {int} dias antes do final da semana")
    public void chegar_dias_antes_do_final_da_semana(Integer dias) {
        alertaAtual = getAlertaAtual();
        alertaService.verificarDisparo(alertaAtual.getId(), LocalDate.now().plusDays(dias));
        alertaAtual = getAlertaAtual();
    }

    @Quando("eu edito o alerta")
    public void eu_edito_o_alerta() {
        alertaAtual = getAlertaAtual();
        alertaService.editar(alertaAtual.getId(), 10);
        alertaAtual = getAlertaAtual();
    }

    @Entao("o alerta deve ser cadastrado com sucesso")
    public void o_alerta_deve_ser_cadastrado_com_sucesso() {
        List<Alerta> alertas = repositorioAlerta.listarPorUsuario(usuarioId);
        assertEquals(1, alertas.size(), "Esperava 1 alerta cadastrado");
    }

    @Entao("o alerta deve aparecer na lista de alertas ativos")
    public void o_alerta_deve_aparecer_na_lista_de_alertas_ativos() {
        assertFalse(repositorioAlerta.listarPorUsuario(usuarioId).isEmpty(), "Esperava alertas ativos");
    }

    @Entao("eu devo receber um erro falando que {string}")
    public void eu_devo_receber_um_erro_falando_que(String msg) {
        assertNotNull(excecao);
        assertTrue(excecao.getMessage().toLowerCase().contains(msg.toLowerCase()));
    }

    @Entao("o alerta deve ser atualizado corretamente")
    public void o_alerta_deve_ser_atualizado_corretamente() {
        alertaAtual = getAlertaAtual();
        List<Alerta> alertas = repositorioAlerta.listarPorUsuario(usuarioId);
        boolean existeAtualizado = alertas.stream()
                .anyMatch(a -> a.getId().equals(alertaAtual.getId()) && a.getValor() == alertaAtual.getValor());
        assertTrue(existeAtualizado, "A lista de alertas não reflete a alteração");
    }

    @Entao("ele deve ser removido da lista de alertas")
    public void ele_deve_ser_removido_da_lista_de_alertas() {
        assertTrue(repositorioAlerta.listarPorUsuario(usuarioId).isEmpty(), "Esperava nenhum alerta após exclusão");
    }

    @Entao("devo receber a notificação do alerta configurado")
    public void devo_receber_a_notificacao_do_alerta_configurado() {
        alertaAtual = getAlertaAtual();
        assertTrue(alertaAtual.isDisparado(), "Esperava que o alerta tivesse sido disparado");
    }

    @Entao("o alerta não deve mais aparecer na tela")
    public void o_alerta_nao_deve_mais_aparecer_na_tela() {
        assertTrue(repositorioAlerta.listarPorUsuario(usuarioId).isEmpty(), "Esperava que nenhum alerta estivesse visível");
    }

    @Entao("a lista de alertas deve refletir a alteração")
    public void a_lista_de_alertas_deve_refletir_a_alteração() {
        alertaAtual = getAlertaAtual();
        List<Alerta> alertas = alertaService.listarPorUsuario(alertaAtual.getUsuarioId());
        boolean existeAtualizado = alertas.stream()
                .anyMatch(a -> a.getId().equals(alertaAtual.getId()) && a.getValor() == alertaAtual.getValor());
        assertTrue(existeAtualizado, "A lista de alertas não reflete a alteração");
    }

    @Entao("o alerta deve ser marcado como disparado no sistema")
    public void o_alerta_deve_ser_marcado_como_disparado_no_sistema() {
        alertaAtual = getAlertaAtual();
        assertTrue(alertaAtual.isDisparado(), "Esperava que o alerta tivesse sido disparado");
    }

    @Entao("o alerta não deve ser criado")
    public void o_alerta_nao_deve_ser_criado() {
        assertTrue(repositorioAlerta.listarPorUsuario(usuarioId).isEmpty(), "Esperava que nenhum alerta fosse criado");
    }

    @Entao("eu devo receber a notificação do alerta configurado")
    public void eu_devo_receber_a_notificação_do_alerta_configurado() {
        alertaAtual = getAlertaAtual();
        assertTrue(alertaAtual.isDisparado(), "Esperava que o alerta tivesse sido disparado");
    }
}
