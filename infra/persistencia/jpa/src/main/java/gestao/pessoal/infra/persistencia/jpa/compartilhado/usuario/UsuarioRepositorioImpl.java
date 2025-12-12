package gestao.pessoal.infra.persistencia.jpa.compartilhado.usuario;


import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioRepositorioApl;
import gestao.pessoal.dominio.principal.compartilhado.usuario.RepositorioUsuario;
import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioResumo;
import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioResumoExpandido;
import gestao.pessoal.dominio.principal.compartilhado.usuario.Usuario;
import gestao.pessoal.infra.persistencia.jpa.JpaMapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class UsuarioRepositorioImpl implements UsuarioRepositorioApl, RepositorioUsuario {


    private final UsuarioJpaRepositorio repo;
    private final JpaMapper mapper;

    public UsuarioRepositorioImpl(UsuarioJpaRepositorio repo, JpaMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }


    @Override
    public void salvar(Usuario usuario) {
        repo.save(mapper.map(usuario, UsuarioJpa.class));
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return repo.findById(id)
                .map(jpa -> mapper.map(jpa, Usuario.class));
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return repo.findByEmail(email)
                .map(jpa -> mapper.map(jpa, Usuario.class));
    }

    @Override
    public boolean existePorEmail(String email) {
        return repo.findByEmail(email).isPresent();
    }

    @Override
    public void remover(UUID id) {
        repo.deleteById(id);
    }

    @Override
    public Optional<UsuarioResumo> buscarResumoPorId(UUID id) {
        return repo.findById(id)
                .map(jpa -> mapper.map(jpa, UsuarioResumo.class));
    }

    @Override
    public Optional<UsuarioResumoExpandido> buscarResumoExpandidoPorId(UUID id) {
        return repo.findById(id)
                .map(jpa -> mapper.map(jpa, UsuarioResumoExpandido.class));
    }

    @Override
    public List<Usuario> buscarPorParteDoNome(String nome) {
        return repo.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(jpa -> mapper.map(jpa, Usuario.class))
                .collect(Collectors.toList());
    }
}