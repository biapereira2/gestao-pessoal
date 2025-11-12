package gestao.pessoal.engajamento.amigo;


import gestao.pessoal.compartilhado.usuario.RepositorioUsuario;
import gestao.pessoal.compartilhado.usuario.Usuario;
import gestao.pessoal.engajamento.perfilSocial.PerfilSocial;
import gestao.pessoal.engajamento.perfilSocial.RepositorioPerfilSocial;
import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class AmizadeService {
    private final RepositorioPerfilSocial repositorioPerfil;
    private final RepositorioUsuario repositorioUsuario;

    public AmizadeService(RepositorioPerfilSocial repositorioPerfil, RepositorioUsuario repositorioUsuario) {
        this.repositorioPerfil = repositorioPerfil;
        this.repositorioUsuario = repositorioUsuario;
    }

    public void adicionarAmigo(UUID usuarioId, String emailAmigo) {
        PerfilSocial perfilUsuario = buscarPerfilOuLancarErro(usuarioId);
        Usuario amigo = repositorioUsuario.buscarPorEmail(emailAmigo)
                .orElseThrow(() -> new IllegalArgumentException("Usuário com email " + emailAmigo + " não encontrado."));
        UUID amigoId = amigo.getId();
        PerfilSocial perfilAmigo = buscarPerfilOuLancarErro(amigoId);
        perfilUsuario.adicionarAmigo(amigoId);
        perfilAmigo.adicionarAmigo(usuarioId);

        repositorioPerfil.salvar(perfilUsuario);
        repositorioPerfil.salvar(perfilAmigo);
    }

    public void removerAmigo(UUID usuarioId, UUID amigoId) {
        PerfilSocial perfilUsuario = buscarPerfilOuLancarErro(usuarioId);
        PerfilSocial perfilAmigo = buscarPerfilOuLancarErro(amigoId);
        perfilUsuario.removerAmigo(amigoId);
        perfilAmigo.removerAmigo(usuarioId);
        repositorioPerfil.salvar(perfilUsuario);
        repositorioPerfil.salvar(perfilAmigo);
    }

    public List<AmigoDTO> listarAmigos(UUID usuarioId) {
        PerfilSocial perfilUsuario = buscarPerfilOuLancarErro(usuarioId);
        Set<UUID> idsDosAmigos = perfilUsuario.getAmigos();
        return idsDosAmigos.stream()
                .map(id -> repositorioUsuario.buscarPorId(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(u -> new AmigoDTO(u.getId(), u.getNome()))
                .collect(Collectors.toList());
    }

    private PerfilSocial buscarPerfilOuLancarErro(UUID usuarioId) {
        return repositorioPerfil.buscarPorUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalStateException("Perfil social para o usuário " + usuarioId + " não encontrado."));
    }
}