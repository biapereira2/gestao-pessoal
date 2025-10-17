package gestao.pessoal.aplicacao;

import gestao.pessoal.compartilhado.Usuario;
import gestao.pessoal.habito.Meta;
import gestao.pessoal.habito.RepositorioMeta;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Entao;

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
    @Dado("que sou um usuário autenticado para metas")
    public void que_sou_um_usuario_autenticado_para_metas() {
        this.usuario = new Usuario("Usuário Teste", "teste@email.com", "123456");
    }

    @Dado("eu já possuo uma meta semanal de {int} hábitos cadastrada")
    public void eu_ja_possuo_uma_meta_semanal_de_habitos_cadastrada(Integer quantidade) {
        metaService.criar(usuario.getId(), UUID.randomUUID(), Meta.Tipo.SEMANAL, "Meta semanal", quantidade);
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
            // Passando null para simular tipo inválido
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

    // --- ENTÃO ---
    @Entao("a meta deve estar cadastrada")
    public void a_meta_deve_estar_cadastrada() {
        List<Meta> metas = metaService.listarPorUsuario(usuario.getId());
        assertFalse(metas.isEmpty());
    }

    @Entao("eu devo receber um erro de meta informando que {string}")
    public void eu_devo_receber_um_erro_de_meta_informando_que(String mensagemDeErro) {
        assertNotNull(excecaoLancada, "Nenhuma exceção foi lançada, mas era esperada.");
        assertTrue(excecaoLancada.getMessage().toLowerCase().contains(mensagemDeErro.toLowerCase()),
                "Esperava erro com mensagem '" + mensagemDeErro + "', mas recebi '" + excecaoLancada.getMessage() + "'");
    }

    @Entao("a lista de metas deve estar vazia")
    public void a_lista_de_metas_deve_estar_vazia() {
        assertEquals(0, metaService.listarPorUsuario(usuario.getId()).size());
    }

    @Entao("a lista de metas deve conter {int} meta")
    public void a_lista_de_metas_deve_conter_meta(Integer quantidade) {
        assertEquals(quantidade, metaService.listarPorUsuario(usuario.getId()).size());
    }
}
