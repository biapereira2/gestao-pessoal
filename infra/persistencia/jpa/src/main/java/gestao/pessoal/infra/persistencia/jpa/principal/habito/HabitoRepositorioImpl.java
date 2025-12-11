package gestao.pessoal.infra.persistencia.jpa.principal.habito;

import gestao.pessoal.aplicacao.principal.habito.*;
import gestao.pessoal.dominio.principal.princ.habito.Habito;
import gestao.pessoal.infra.persistencia.jpa.JpaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class HabitoRepositorioImpl implements HabitoRepositorioApl {

    private final HabitoJpaRepositorio repo;
    private final JpaMapper mapper;

    public HabitoRepositorioImpl(HabitoJpaRepositorio repo, JpaMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public void salvar(Habito habito) {
        repo.save(mapper.map(habito, HabitoJpa.class));
    }

    @Override
    public Optional<Habito> buscarPorId(UUID id) {
        return repo.findById(id)
                .map(jpa -> mapper.map(jpa, Habito.class));
    }

    @Override
    public List<HabitoResumo> listarResumosPorUsuario(UUID usuarioId) {
        return repo.findByUsuarioId(usuarioId)
                .stream()
                .map(jpa -> new HabitoResumo(
                        jpa.getId(),
                        jpa.getNome(),
                        jpa.getCategoria(),
                        jpa.getFrequencia()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<HabitoResumoExpandido> listarResumosExpandidoPorUsuario(UUID usuarioId) {
        return repo.findByUsuarioId(usuarioId)
                .stream()
                .map(jpa -> new HabitoResumoExpandido(
                        jpa.getId(),
                        jpa.getNome(),
                        jpa.getDescricao(),
                        jpa.getCategoria(),
                        jpa.getFrequencia(),
                        jpa.getPontuacaoCheckin()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void remover(UUID id) {
        repo.deleteById(id);
    }
}
