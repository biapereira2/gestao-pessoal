package gestao.pessoal.infra.persistencia.jpa.compartilhado.usuario;

// Imports das Interfaces que vamos implementar
import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioRepositorioApl; // Interface da Aplicação
import gestao.pessoal.dominio.principal.compartilhado.usuario.RepositorioUsuario; // Interface do Domínio

// Imports do Domínio e Infra
import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioResumo;
import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioResumoExpandido;
import gestao.pessoal.dominio.principal.compartilhado.usuario.Usuario;
import gestao.pessoal.infra.persistencia.jpa.JpaMapper;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository // Essencial para o Spring encontrar
public class UsuarioRepositorioImpl implements UsuarioRepositorioApl, RepositorioUsuario {
    // ^^^ AQUI ESTÁ O SEGREDO: Implementar RepositorioUsuario

    private final UsuarioJpaRepositorio repo;
    private final JpaMapper mapper;

    public UsuarioRepositorioImpl(UsuarioJpaRepositorio repo, JpaMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    // --- Métodos do RepositorioUsuario (Domínio) ---

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

    // --- Métodos Extras do UsuarioRepositorioApl (Se houver na interface de aplicação) ---

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
}