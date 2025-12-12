package gestao.pessoal.aplicacao.compartilhado.usuario;

import gestao.pessoal.dominio.principal.compartilhado.usuario.Usuario;
import org.springframework.stereotype.Service;
import gestao.pessoal.dominio.principal.engajamento.perfilSocial.PerfilSocial;
import gestao.pessoal.dominio.principal.engajamento.perfilSocial.RepositorioPerfilSocial;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceApl {

    private final UsuarioRepositorioApl repositorio;
    private final RepositorioPerfilSocial repositorioSocial;

    public UsuarioServiceApl(UsuarioRepositorioApl repositorio, RepositorioPerfilSocial repositorioSocial) {
        this.repositorio = repositorio;
        this.repositorioSocial = repositorioSocial;
    }

    public void criar(Usuario usuario) {
        // 1. Salva o usuário no banco (Shared Kernel)
        repositorio.salvar(usuario);

        // 2. Cria automaticamente o Perfil Social (Engajamento)
        // Isso garante que o usuário já possa receber convites de amizade imediatamente
        if (repositorioSocial.buscarPorUsuarioId(usuario.getId()).isEmpty()) {
            PerfilSocial novoPerfil = new PerfilSocial(usuario.getId());
            repositorioSocial.salvar(novoPerfil);
        }
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
