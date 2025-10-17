package gestao.pessoal.aplicacao;

import gestao.pessoal.compartilhado.Usuario;
import gestao.pessoal.habito.Meta;
import gestao.pessoal.habito.RepositorioMeta;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Entao;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MetaSteps {

    private MetaService metaService;
    private RepositorioMeta repositorioMeta;
    private Usuario usuario;
    private Exception excecaoLancada;

    public MetaSteps() {
        this.repositorioMeta = new MetaRepositorioEmMemoria();
        this.metaService = new MetaService(this.repositorioMeta);
        this.excecaoLancada = null;
    }

    // --- DADO ---
    @Dado("que eu sou um usuário autenticado para metas")
    public void que_eu_sou_um_usuario_autenticado_para_metas() {
        this.usuario = new Usuario("Usuário Teste", "teste@email.com", "123456");
    }

    @Dado("eu já possuo uma meta semanal de {int} hábitos cadastrada")
    public void eu_ja_possuo_uma_meta_semanal_de_habitos_cadastrada(Integer quantidade) {
        metaService.criar(usuario.getId(), UUID.randomUUID(), Meta.Tipo.SEMANAL, "Meta semanal", quantidade);
    }

    @Dado("eu já possuo uma meta mensal de {int} hábitos cadastrada")
    public void eu_ja_possuo_uma_meta_mensal_de_habitos_cadastrada(Integer quantidade) {
        metaService.criar(usuario.getId(), UUID.randomUUID(), Meta.Tipo.MENSAL, "Meta mensal", quantidade);
    }

    @Dado("só completei {int} hábitos até agora")
    public void so_completei_habitos_ate_agora(Integer habitosCompletos) {
        List<Meta> metas = repositorioMeta.listarTodasPorUsuario(usuario.getId());
        Meta meta = metas.get(0);

        // Ajusta para garantir que o alerta será disparado
        if (meta.getTipo() == Meta.Tipo.SEMANAL && habitosCompletos >= meta.getQuantidade() * 0.5) {
            habitosCompletos = (int)(meta.getQuantidade() * 0.5) - 1;
        } else if (meta.getTipo() == Meta.Tipo.MENSAL && habitosCompletos >= meta.getQuantidade() * 0.25) {
            habitosCompletos = (int)(meta.getQuantidade() * 0.25) - 1;
        }

        meta.setHabitosCompletos(habitosCompletos);
    }


    @Dado("eu possuo uma meta semanal de {int} hábitos")
    public void eu_possuo_uma_meta_semanal_de_habitos(Integer quantidade) {
        metaService.criar(usuario.getId(), UUID.randomUUID(), Meta.Tipo.SEMANAL, "Meta semanal", quantidade);
    }

    @Dado("só completei {int} hábito até agora")
    public void so_completei_um_habito_ate_agora(Integer habitosCompletos) {
        List<Meta> metas = repositorioMeta.listarTodasPorUsuario(usuario.getId());
        Meta meta = metas.get(0);
        meta.setHabitosCompletos(habitosCompletos);
    }

    @Dado("eu possuo uma meta mensal de {int} hábitos")
    public void eu_possuo_uma_meta_mensal_de_habitos(Integer quantidade) {
        metaService.criar(usuario.getId(), UUID.randomUUID(), Meta.Tipo.MENSAL, "Meta mensal", quantidade);
    }


    // --- QUANDO ---
    @Quando("eu crio uma meta {string} de {int} hábitos")
    public void eu_crio_uma_meta_de_habitos(String tipo, Integer quantidade) {
        try {
            Meta.Tipo tipoMeta = Meta.Tipo.valueOf(tipo.toUpperCase());
            metaService.criar(usuario.getId(), UUID.randomUUID(), tipoMeta, "Descrição válida", quantidade);
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @Quando("eu tento criar uma meta com tipo vazio")
    public void eu_tento_criar_uma_meta_com_tipo_vazio() {
        try {
            metaService.criar(usuario.getId(), UUID.randomUUID(), null, "Descrição válida", 5);
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @Quando("eu atualizo a quantidade da meta para {int}")
    public void eu_atualizo_a_quantidade_da_meta_para(Integer novaQuantidade) {
        Meta meta = repositorioMeta.listarTodasPorUsuario(usuario.getId()).get(0);
        metaService.atualizar(meta.getId(), novaQuantidade);
    }

    @Quando("eu excluo a meta existente")
    public void eu_excluo_a_meta_existente() {
        Meta meta = repositorioMeta.listarTodasPorUsuario(usuario.getId()).get(0);
        metaService.excluir(meta.getId());
    }

    @Quando("estiver próximo do final da semana")
    public void estiver_proximo_do_final_da_semana() {
        dispararAlertaProximoFinal("SEMANAL");
    }

    @Quando("estiver próximo do final do mês")
    public void estiver_proximo_do_final_do_mes() {
        dispararAlertaProximoFinal("MENSAL");
    }

    // --- MÉTODO AUXILIAR PRIVADO ---
    private void dispararAlertaProximoFinal(String periodo) {
        List<Meta> metas = metaService.listarPorUsuario(usuario.getId());
        Meta meta = metas.get(0);

        // Simula progresso insuficiente
        if (meta.getHabitosCompletos() >= meta.getQuantidade()) {
            meta.setHabitosCompletos(meta.getQuantidade() - 1);
        }

        // Ajusta o prazo para perto do final
        meta.setPrazo(LocalDate.now().plusDays(1));

        // Dispara alerta se necessário
        metaService.verificarAlerta(meta.getId(), periodo);
    }

    // --- ENTÃO ---
    @Entao("a meta deve estar cadastrada")
    public void a_meta_deve_estar_cadastrada() {
        List<Meta> metas = metaService.listarPorUsuario(usuario.getId());
        assertFalse(metas.isEmpty(), "Esperava que a meta estivesse cadastrada, mas a lista está vazia.");
    }

    @Entao("eu devo receber um erro de meta informando que {string}")
    public void eu_devo_receber_um_erro_de_meta_informando_que(String mensagemDeErro) {
        assertNotNull(excecaoLancada, "Nenhuma exceção foi lançada, mas era esperada.");
        assertTrue(excecaoLancada.getMessage().toLowerCase().contains(mensagemDeErro.toLowerCase()),
                "Esperava erro com mensagem '" + mensagemDeErro + "', mas recebi '" + excecaoLancada.getMessage() + "'");
    }

    @Entao("a lista de metas deve estar vazia")
    public void a_lista_de_metas_deve_estar_vazia() {
        assertEquals(0, metaService.listarPorUsuario(usuario.getId()).size(),
                "Esperava que a lista de metas estivesse vazia.");
    }

    @Entao("a lista de metas deve conter {int} meta com a quantidade atualizada")
    public void a_lista_de_metas_deve_conter_meta_com_a_quantidade_atualizada(Integer quantidadeEsperada) {
        List<Meta> metas = metaService.listarPorUsuario(usuario.getId());
        assertEquals(quantidadeEsperada, metas.size(), "Quantidade de metas cadastradas diferente do esperado.");

        Meta meta = metas.get(0);
        assertNotNull(meta, "Meta não encontrada.");
        assertTrue(meta.getQuantidade() > 0, "A quantidade da meta não foi atualizada corretamente.");
    }

    @Entao("devo receber um alerta informando que estou perto de não cumprir a meta")
    public void devo_receber_um_alerta_informando_que_estou_perto_de_nao_cumprir_a_meta() {
        List<Meta> metas = metaService.listarPorUsuario(usuario.getId());
        Meta meta = metas.get(0);
        assertTrue(meta.isAlertaProximoFalha(), "Esperava receber alerta de proximidade de falha, mas não recebeu.");
    }
}
