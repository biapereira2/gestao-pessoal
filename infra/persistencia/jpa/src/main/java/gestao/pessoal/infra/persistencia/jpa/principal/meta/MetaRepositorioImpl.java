package gestao.pessoal.infra.persistencia.jpa.principal.meta;

import gestao.pessoal.aplicacao.principal.meta.MetaRepositorioApl;
import gestao.pessoal.aplicacao.principal.meta.MetaResumo;
import gestao.pessoal.aplicacao.principal.meta.MetaResumoExpandido;
import gestao.pessoal.infra.persistencia.jpa.JpaMapper;
import gestao.pessoal.dominio.principal.princ.meta.Meta;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class MetaRepositorioImpl implements MetaRepositorioApl {

    private final MetaJpaRepositorio metaJpaRepositorio;
    private final JpaMapper mapper;

    public MetaRepositorioImpl(MetaJpaRepositorio metaJpaRepositorio, JpaMapper mapper) {
        this.metaJpaRepositorio = metaJpaRepositorio;
        this.mapper = mapper;
    }

    @Override
    public void salvar(Meta meta) {
        MetaJpa jpa = mapper.map(meta, MetaJpa.class);
        metaJpaRepositorio.save(jpa);
    }

    @Override
    public Optional<Meta> buscarPorId(UUID id) {
        return metaJpaRepositorio.findById(id)
                .map(jpa -> mapper.map(jpa, Meta.class));
    }

    @Override
    public List<MetaResumo> listarResumosPorUsuario(UUID usuarioId) {
        List<MetaJpa> metas = metaJpaRepositorio.findByUsuarioId(usuarioId);
        return metas.stream()
                .map(jpa -> mapper.map(jpa, MetaResumo.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MetaResumoExpandido> listarResumosExpandidoPorUsuario(UUID usuarioId) {
        List<MetaJpa> metas = metaJpaRepositorio.findByUsuarioId(usuarioId);
        // O JpaMapper deve estar configurado para mapear o List<UUID> habitosIds do MetaJpa para MetaResumoExpandido.
        return metas.stream()
                .map(jpa -> mapper.map(jpa, MetaResumoExpandido.class))
                .collect(Collectors.toList());
    }

    @Override
    public void remover(UUID id) {
        metaJpaRepositorio.deleteById(id);
    }
}