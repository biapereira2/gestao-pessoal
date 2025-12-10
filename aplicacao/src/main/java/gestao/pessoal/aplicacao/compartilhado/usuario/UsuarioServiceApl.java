package gestao.pessoal.aplicacao.compartilhado.usuario;

import gestao.pessoal.dominio.principal.compartilhado.usuario.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceApl {

    private final UsuarioRepositorioApl repositorio;

    public UsuarioServiceApl(UsuarioRepositorioApl repositorio) {
        this.repositorio = repositorio;
    }

    public void criar(Usuario usuario) {
        repositorio.salvar(usuario);
    }

    public Optional<Usuario> buscarPorId(UUID id) {
        return repositorio.buscarPorId(id);
    }

    public Optional<UsuarioResumo> buscarResumo(UUID id) {
        return repositorio.buscarResumoPorId(id);
    }

    public Optional<UsuarioResumoExpandido> buscarResumoExpandido(UUID id) {
        return repositorio.buscarResumoExpandidoPorId(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return repositorio.buscarPorEmail(email);
    }


    public void atualizar(Usuario usuario) {
        repositorio.salvar(usuario);
    }

    public void remover(UUID id) {
        repositorio.remover(id);
    }
}
