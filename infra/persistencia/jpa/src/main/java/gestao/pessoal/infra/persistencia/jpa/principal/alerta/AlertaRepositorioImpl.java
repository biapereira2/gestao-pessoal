package gestao.pessoal.infra.persistencia.jpa.principal.alerta;

import gestao.pessoal.aplicacao.principal.alerta.*;
import gestao.pessoal.dominio.principal.princ.alerta.Alerta;
import gestao.pessoal.infra.persistencia.jpa.JpaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class AlertaRepositorioImpl implements AlertaRepositorioApl {

    private final AlertaJpaRepositorio repo;
    private final JpaMapper mapper;

    public AlertaRepositorioImpl(AlertaJpaRepositorio repo, JpaMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public void salvar(Alerta alerta) {
        repo.save(mapper.map(alerta, AlertaJpa.class));
    }

    @Override
    public Optional<Alerta> buscarPorId(UUID id) {
        return repo.findById(id).map(jpa -> mapper.map(jpa, Alerta.class));
    }

    @Override
    public List<AlertaResumo> listarPorUsuario(UUID usuarioId) {
        return repo.findByUsuarioId(usuarioId)
                .stream()
                .map(jpa -> new AlertaResumo(
                        jpa.getId(),
                        jpa.getDescricao(),
                        jpa.getCondicao(),
                        jpa.getValor(),
                        jpa.isDisparado()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<AlertaResumoExpandido> listarResumosExpandidoPorUsuario(UUID usuarioId) {
        return repo.findByUsuarioId(usuarioId)
                .stream()
                .map(jpa -> new AlertaResumoExpandido(
                        jpa.getId(),
                        jpa.getUsuarioId(),
                        jpa.getMetaId(),
                        jpa.getCondicao(),
                        jpa.getValor(),
                        jpa.getDescricao(),
                        jpa.isDisparado(),
                        jpa.getDataCriacao()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void remover(UUID id) {
        repo.deleteById(id);
    }
}
