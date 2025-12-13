package gestao.pessoal.infra.persistencia.jpa.engajamento.progressoUsuario;

import gestao.pessoal.dominio.principal.engajamento.progressoUsuario.ProgressoUsuario;
import gestao.pessoal.dominio.principal.engajamento.progressoUsuario.RepositorioProgressoUsuario;
import gestao.pessoal.infra.persistencia.jpa.JpaMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ProgressoUsuarioRepositorioImpl
        implements RepositorioProgressoUsuario {

    private final ProgressoUsuarioJpaRepositorio jpaRepositorio;
    private final JpaMapper mapper;

    public ProgressoUsuarioRepositorioImpl(
            ProgressoUsuarioJpaRepositorio jpaRepositorio,
            JpaMapper mapper
    ) {
        this.jpaRepositorio = jpaRepositorio;
        this.mapper = mapper;
    }

    @Override
    public void salvar(ProgressoUsuario progresso) {
        ProgressoUsuarioJpa jpa =
                mapper.map(progresso, ProgressoUsuarioJpa.class);
        jpaRepositorio.save(jpa);
    }

    @Override
    public Optional<ProgressoUsuario> buscarPorUsuarioId(UUID usuarioId) {
        return jpaRepositorio.findById(usuarioId)
                .map(jpa -> mapper.map(jpa, ProgressoUsuario.class));
    }

    @Override
    public boolean existeParaUsuario(UUID usuarioId) {
        return jpaRepositorio.existsById(usuarioId);
    }
}

