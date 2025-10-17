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

    public Meta criar(UUID usuarioId, UUID habitoId, Meta.Tipo tipo, String descricao, int quantidade) {
        Meta meta = new Meta(usuarioId, habitoId, tipo, descricao, quantidade);
        repositorioMeta.salvar(meta);
        return meta;
    }

    public void atualizar(UUID metaId, int novaQuantidade) {
        Meta meta = repositorioMeta.buscarPorId(metaId)
                .orElseThrow(() -> new IllegalArgumentException("Meta com ID " + metaId + " não encontrada."));
        meta.atualizarQuantidade(novaQuantidade);
        repositorioMeta.salvar(meta);
    }

    public List<Meta> listarPorUsuario(UUID usuarioId) {
        return repositorioMeta.listarTodasPorUsuario(usuarioId);
    }

    public void verificarAlertas(UUID usuarioId) {
        List<Meta> metas = repositorioMeta.listarTodasPorUsuario(usuarioId);
        metas.stream()
                .filter(Meta::estaPertoDeFalhar)
                .forEach(meta ->
                        System.out.println("⚠️ Atenção! Você está perto de falhar a meta: " + meta.getDescricao()));
    }

    public void excluir(UUID metaId) {
        repositorioMeta.excluir(metaId);
    }
}
