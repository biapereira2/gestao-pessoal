package gestao.pessoal.infra.persistencia.jpa.compartilhado.usuario;

import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioRepositorioApl;
import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioResumo;
import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioResumoExpandido;
import gestao.pessoal.dominio.principal.compartilhado.usuario.Usuario;
import gestao.pessoal.infra.persistencia.jpa.JpaMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UsuarioRepositorioImpl implements UsuarioRepositorioApl {

    private final UsuarioJpaRepositorio repositorioJpa;
    private final JpaMapper mapper;

    public UsuarioRepositorioImpl(UsuarioJpaRepositorio repositorioJpa, JpaMapper mapper) {
        this.repositorioJpa = repositorioJpa;
        this.mapper = mapper;
    }

    @Override
    public void salvar(Usuario usuario) {
        UsuarioJpa entidade = mapper.map(usuario, UsuarioJpa.class);
        repositorioJpa.save(entidade);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return repositorioJpa.findById(id)
                .map(jpa -> mapper.map(jpa, Usuario.class));
    }

    @Override
    public Optional<UsuarioResumo> buscarResumoPorId(UUID id) {
        return repositorioJpa.findById(id)
                .map(jpa -> mapper.map(jpa, UsuarioResumo.class));
    }

    @Override
    public Optional<UsuarioResumoExpandido> buscarResumoExpandidoPorId(UUID id) {
        return repositorioJpa.findById(id)
                .map(jpa -> mapper.map(jpa, UsuarioResumoExpandido.class));
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return repositorioJpa.findByEmail(email)
                .map(jpa -> mapper.map(jpa, Usuario.class));
    }

    @Override
    public void remover(UUID id) {
        repositorioJpa.deleteById(id);
    }
}
