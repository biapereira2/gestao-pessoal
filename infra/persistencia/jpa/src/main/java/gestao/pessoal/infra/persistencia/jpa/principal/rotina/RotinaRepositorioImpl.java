package gestao.pessoal.infra.persistencia.jpa.principal.rotina;

import gestao.pessoal.aplicacao.principal.rotina.RotinaRepositorioApl;
import gestao.pessoal.aplicacao.principal.rotina.RotinaResumo;
import gestao.pessoal.aplicacao.principal.rotina.RotinaResumoExpandido;
import gestao.pessoal.infra.persistencia.jpa.JpaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class RotinaRepositorioImpl implements RotinaRepositorioApl {

    private final RotinaJpaRepositorio rotinaJpaRepositorio;
    private final JpaMapper mapper;

    public RotinaRepositorioImpl(RotinaJpaRepositorio rotinaJpaRepositorio, JpaMapper mapper) {
        this.rotinaJpaRepositorio = rotinaJpaRepositorio;
        this.mapper = mapper;
    }

    @Override
    public void salvar(RotinaResumoExpandido rotina) {
        RotinaJpa jpa = mapper.map(rotina, RotinaJpa.class);
        rotinaJpaRepositorio.save(jpa);
    }

    @Override
    public Optional<RotinaResumoExpandido> buscarPorId(UUID id) {
        return rotinaJpaRepositorio.findById(id)
                .map(jpa -> mapper.map(jpa, RotinaResumoExpandido.class));
    }

    @Override
    public List<RotinaResumo> listarResumosPorUsuario(UUID usuarioId) {
        List<RotinaJpa> rotinas = rotinaJpaRepositorio.findByUsuarioId(usuarioId);
        return rotinas.stream()
                .map(jpa -> mapper.map(jpa, RotinaResumo.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RotinaResumoExpandido> listarResumosExpandidoPorUsuario(UUID usuarioId) {
        List<RotinaJpa> rotinas = rotinaJpaRepositorio.findByUsuarioId(usuarioId);
        return rotinas.stream()
                .map(jpa -> mapper.map(jpa, RotinaResumoExpandido.class))
                .collect(Collectors.toList());
    }

    @Override
    public void remover(UUID id) {
        rotinaJpaRepositorio.deleteById(id);
    }
}
