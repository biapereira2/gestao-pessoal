package gestao.pessoal.aplicacao;

import gestao.pessoal.compartilhado.RepositorioUsuario;
import gestao.pessoal.compartilhado.Usuario;
import gestao.pessoal.habito.Habito;
import gestao.pessoal.habito.Meta;
import gestao.pessoal.habito.RepositorioHabito;
import gestao.pessoal.habito.RepositorioMeta;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

// =================================================================
// IMPLEMENTAÇÃO MOCK (FAKE REPOSITÓRIO)
// =================================================================
class FakeRepositorioMeta implements RepositorioMeta {

    private final Map<UUID, Meta> metas = new HashMap<>();

    @Override
    public void salvar(Meta meta) {
        metas.put(meta.getId(), meta);
    }

    @Override
    public Optional<Meta> buscarPorId(UUID metaId) {
        return Optional.ofNullable(metas.get(metaId));
    }

    @Override
    public List<Meta> listarPorUsuario(UUID usuarioId) {
        return metas.values().stream()
                .filter(m -> m.getUsuarioId().equals(usuarioId))
                .collect(Collectors.toList());
    }

    @Override
    public void remover(UUID metaId) {
        metas.remove(metaId);
    }

    public void limpar() {
        metas.clear();
    }
}

public class MetaSteps {

    private FakeRepositorioMeta repositorioMeta;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioHabito repositorioHabito;
    private Usuario usuario;
    private Exception excecaoLancada;

    public MetaSteps() {
        this.repositorioHabito = new FakeRepositorioHabito();
        this.repositorioMeta = new FakeRepositorioMeta();
        this.repositorioUsuario = new FakeRepositorioUsuario();
        this.excecaoLancada = null;
    }

    @Before
    public void setup() {
        this.repositorioMeta = new FakeRepositorioMeta();
        this.repositorioMeta.limpar();
        this.repositorioHabito = new FakeRepositorioHabito();
        this.repositorioUsuario = new FakeRepositorioUsuario();
        this.excecaoLancada = null;
    }

    private void garantirUsuario() {
        if (usuario == null) {
            usuario = new Usuario("Usuário Teste", "teste@email.com", "123456");
            repositorioUsuario.salvar(usuario);
        }
    }

    private MetaService criarMetaServiceComUsuarioMock() {
        RepositorioUsuario repoMock = new RepositorioUsuario() {
            @Override
            public void salvar(Usuario usuario) { }
            @Override
            public Optional<Usuario> buscarPorId(UUID id) {
                if (id.equals(usuario.getId())) return Optional.of(usuario);
                return Optional.empty();
            }
            @Override
            public Optional<Usuario> buscarPorEmail(String email) { return Optional.of(usuario); }
            @Override
            public boolean existePorEmail(String email) { return true; }
        };
        return new MetaService(repositorioMeta, repoMock, repositorioHabito);
    }

    @Dado("que eu sou um usuário autenticado para metas")
    public void que_eu_sou_um_usuario_autenticado_para_metas() {
        garantirUsuario();
    }

    @Dado("eu já possuo uma meta semanal de {int} hábitos cadastrada")
    public void eu_ja_possuo_uma_meta_semanal_de_habitos_cadastrada(Integer quantidade) {
        garantirUsuario();
        repositorioMeta.limpar();
        List<UUID> habitosIds = new ArrayList<>();

        for (int i = 0; i < quantidade; i++) {
            Habito habito = new Habito(usuario.getId(), "Hábito " + (i + 1), "Descrição " + (i + 1),
                    "Categoria " + (i + 1), "Diaria");
            repositorioHabito.salvar(habito);
            habitosIds.add(habito.getId());
        }

        MetaService metaService = criarMetaServiceComUsuarioMock();
        metaService.criar(usuario.getId(), habitosIds, Meta.Tipo.SEMANAL, "Meta semanal");
    }


    @Dado("eu já possuo uma meta mensal de {int} hábitos cadastrada")
    public void eu_ja_possuo_uma_meta_mensal_de_habitos_cadastrada(Integer quantidade) {
        garantirUsuario();
        repositorioMeta.limpar();
        List<UUID> habitosIds = new ArrayList<>();

        for (int i = 0; i < quantidade; i++) {
            Habito habito = new Habito(usuario.getId(), "Hábito " + (i + 1), "Descrição " + (i + 1),
                    "Categoria " + (i + 1), "Diaria");
            repositorioHabito.salvar(habito);
            habitosIds.add(habito.getId());
        }

        MetaService metaService = criarMetaServiceComUsuarioMock();
        metaService.criar(usuario.getId(), habitosIds, Meta.Tipo.MENSAL, "Meta mensal");
    }


    @Dado("eu possuo uma meta semanal de {int} hábitos")
    public void eu_possuo_uma_meta_semanal_de_habitos(Integer quantidade) {
        garantirUsuario();
        criarMetaComHabitos(quantidade, Meta.Tipo.SEMANAL, "Meta semanal");
    }

    @Dado("eu possuo uma meta mensal de {int} hábitos")
    public void eu_possuo_uma_meta_mensal_de_habitos(Integer quantidade) {
        garantirUsuario();
        criarMetaComHabitos(quantidade, Meta.Tipo.MENSAL, "Meta mensal");
    }

    private void criarMetaComHabitos(int quantidade, Meta.Tipo tipo, String descricao) {
        repositorioMeta.limpar();
        List<UUID> habitosIds = new ArrayList<>();

        for (int i = 0; i < quantidade; i++) {
            Habito habito = new Habito(usuario.getId(), "Hábito " + (i + 1), "Descrição", "Categoria", "Diaria");
            repositorioHabito.salvar(habito);
            habitosIds.add(habito.getId());
        }

        MetaService metaService = criarMetaServiceComUsuarioMock();
        metaService.criar(usuario.getId(), habitosIds, tipo, descricao);
    }


    @Dado("só completei {int} hábito até agora")
    @Dado("só completei {int} hábitos até agora")
    public void so_completei_habitos_ate_agora(Integer habitosCompletos) {
        List<Meta> metas = repositorioMeta.listarPorUsuario(usuario.getId());
        if (!metas.isEmpty()) {
            Meta meta = metas.get(0);
            meta.setHabitosCompletos(habitosCompletos);
        }
    }

    @Quando("eu crio uma meta {string} de {int} hábitos")
    public void eu_crio_uma_meta_de_habitos(String tipo, Integer quantidade) {
        try {
            garantirUsuario();

            Meta.Tipo tipoMeta = Meta.Tipo.valueOf(tipo.toUpperCase());
            criarMetaComHabitos(quantidade, tipoMeta, "Descrição válida");
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @Quando("eu tento criar uma meta com tipo vazio")
    public void eu_tento_criar_uma_meta_com_tipo_vazio() {
        try {
            garantirUsuario();

            List<UUID> habitosIds = new ArrayList<>();
            Habito habito = new Habito(usuario.getId(), "Hábito Teste", "Descrição", "Categoria", "Diaria");
            repositorioHabito.salvar(habito);
            habitosIds.add(habito.getId());

            MetaService metaService = criarMetaServiceComUsuarioMock();
            metaService.criar(usuario.getId(), habitosIds, null, "Descrição válida");
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @Quando("eu atualizo a quantidade da meta para {int}")
    public void eu_atualizo_a_quantidade_da_meta_para(Integer novaQuantidade) {
        List<Meta> metas = repositorioMeta.listarPorUsuario(usuario.getId());
        if (!metas.isEmpty()) {
            Meta meta = metas.get(0);
            MetaService metaService = criarMetaServiceComUsuarioMock();
            metaService.atualizar(meta.getId(), novaQuantidade);
        }
    }

    @Quando("eu excluo a meta existente")
    public void eu_excluo_a_meta_existente() {
        List<Meta> metas = repositorioMeta.listarPorUsuario(usuario.getId());
        if (!metas.isEmpty()) {
            Meta meta = metas.get(0);
            MetaService metaService = criarMetaServiceComUsuarioMock();
            metaService.excluir(meta.getId());
        }
    }

    @Quando("estiver próximo do final da semana")
    public void estiver_proximo_do_final_da_semana() {
        List<Meta> metas = repositorioMeta.listarPorUsuario(usuario.getId());
        if (!metas.isEmpty()) {
            Meta meta = metas.get(0);
            meta.setPrazo(LocalDate.now().plusDays(1));
            MetaService metaService = criarMetaServiceComUsuarioMock();
            metaService.verificarAlerta(meta.getId(), "SEMANAL");
        }
    }

    @Quando("estiver próximo do final do mês")
    public void estiver_proximo_do_final_do_mes() {
        List<Meta> metas = repositorioMeta.listarPorUsuario(usuario.getId());
        if (!metas.isEmpty()) {
            Meta meta = metas.get(0);
            meta.setPrazo(LocalDate.now().plusDays(1));
            meta.setHabitosCompletos(meta.getQuantidade() - 1);
            MetaService metaService = criarMetaServiceComUsuarioMock();
            metaService.verificarAlerta(meta.getId(), "MENSAL");
        }
    }

    @Entao("a meta deve estar cadastrada")
    public void a_meta_deve_estar_cadastrada() {
        List<Meta> metas = repositorioMeta.listarPorUsuario(usuario.getId());
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
        assertEquals(0, repositorioMeta.listarPorUsuario(usuario.getId()).size(),
                "Esperava que a lista de metas estivesse vazia.");
    }

    @Entao("devo receber um alerta informando que estou perto de não cumprir a meta")
    public void devo_receber_um_alerta_informando_que_estou_perto_de_não_cumprir_a_meta() {
        List<Meta> metas = repositorioMeta.listarPorUsuario(usuario.getId());
        metas.forEach(Meta::dispararAlertaSeNecessario);
        boolean alertaAtivo = metas.stream().anyMatch(Meta::isAlertaProximoFalha);
        assertTrue(alertaAtivo, "Esperava receber um alerta de que a meta está perto de não ser cumprida.");
    }

    @Entao("a lista de metas deve conter {int} meta com a quantidade atualizada")
    public void a_lista_de_metas_deve_conter_meta_com_a_quantidade_atualizada(Integer quantidadeEsperada) {
        List<Meta> metas = repositorioMeta.listarPorUsuario(usuario.getId());
        assertEquals(quantidadeEsperada, metas.size(), "Quantidade de metas cadastradas diferente do esperado.");

        Meta meta = metas.get(0);
        assertNotNull(meta, "Meta não encontrada.");
        assertTrue(meta.getQuantidade() > 0, "A quantidade da meta não foi atualizada corretamente.");
    }
}
