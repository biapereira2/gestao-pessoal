package gestao.pessoal.aplicacao;

import gestao.pessoal.habito.Meta;
import gestao.pessoal.habito.RepositorioMeta;

import java.util.*;
import java.util.stream.Collectors;

// =================================================================
// IMPLEMENTAÇÃO MOCK (FAKE REPOSITÓRIO)
// =================================================================
public class FakeRepositorioMeta implements RepositorioMeta {

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
