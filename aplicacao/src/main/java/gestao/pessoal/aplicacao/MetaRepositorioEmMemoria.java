package gestao.pessoal.aplicacao;

import gestao.pessoal.habito.Meta;
import gestao.pessoal.habito.RepositorioMeta;

import java.util.*;
import java.util.stream.Collectors;

public class MetaRepositorioEmMemoria implements RepositorioMeta {

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
    public List<Meta> listarTodasPorUsuario(UUID usuarioId) {
        return metas.values().stream()
                .filter(meta -> meta.getUsuarioId().equals(usuarioId))
                .collect(Collectors.toList());
    }

    @Override
    public void excluir(UUID metaId) {
        metas.remove(metaId);
    }
}
