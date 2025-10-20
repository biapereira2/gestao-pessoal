package gestao.pessoal.aplicacao;

import gestao.pessoal.habito.Meta;
import gestao.pessoal.habito.RepositorioMeta;

import java.util.List;
import java.util.UUID;

public class MetaService {

    private final RepositorioMeta repositorioMeta;

    public MetaService(RepositorioMeta repositorioMeta) {
        this.repositorioMeta = repositorioMeta;
    }

    // --- Criar meta ---
    public Meta criar(UUID usuarioId, UUID habitoId, Meta.Tipo tipo, String descricao, int quantidade) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo inválido");
        }
        Meta meta = new Meta(usuarioId, habitoId, tipo, descricao, quantidade);
        repositorioMeta.salvar(meta);
        return meta;
    }

    // --- Atualizar meta ---
    public void atualizar(UUID metaId, int novaQuantidade) {
        Meta meta = repositorioMeta.buscarPorId(metaId)
                .orElseThrow(() -> new IllegalArgumentException("Meta com ID " + metaId + " não encontrada."));
        meta.atualizarQuantidade(novaQuantidade);
        repositorioMeta.salvar(meta);
    }

    // --- Listar metas do usuário ---
    public List<Meta> listarPorUsuario(UUID usuarioId) {
        return repositorioMeta.listarTodasPorUsuario(usuarioId);
    }

    // --- Excluir meta ---
    public void excluir(UUID metaId) {
        repositorioMeta.excluir(metaId);
    }

    // --- Verificar alertas de todas as metas do usuário ---
    public void verificarAlertas(UUID usuarioId) {
        List<Meta> metas = repositorioMeta.listarTodasPorUsuario(usuarioId);
        metas.stream()
                .filter(Meta::estaPertoDeFalhar)
                .forEach(meta ->
                        System.out.println("⚠️ Atenção! Você está perto de falhar a meta: " + meta.getDescricao()));
    }

    // --- Verificar alerta de uma meta específica ---
    public void verificarAlerta(UUID metaId, String periodo) {
        Meta meta = repositorioMeta.buscarPorId(metaId)
                .orElseThrow(() -> new IllegalArgumentException("Meta com ID " + metaId + " não encontrada."));

        // Atualiza o flag de alerta baseado na quantidade de hábitos completados
        meta.dispararAlertaSeNecessario();

        if (meta.isAlertaProximoFalha()) {
            System.out.println("⚠️ Atenção! Você está perto de falhar a meta: " + meta.getDescricao());
        }
    }
}
