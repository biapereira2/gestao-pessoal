package gestao.pessoal.dominio.principal.engajamento.amigo;

import gestao.pessoal.dominio.principal.compartilhado.usuario.RepositorioUsuario;
import gestao.pessoal.dominio.principal.compartilhado.usuario.Usuario;
import gestao.pessoal.dominio.principal.engajamento.amigo.evento.AmizadeCriadaEvent;
import gestao.pessoal.dominio.principal.engajamento.perfilSocial.PerfilSocial;
import gestao.pessoal.dominio.principal.engajamento.perfilSocial.RepositorioPerfilSocial;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AmizadeService {

    private final RepositorioPerfilSocial repositorioPerfil;
    private final RepositorioUsuario repositorioUsuario;
    private final ApplicationEventPublisher eventPublisher;

    public AmizadeService(RepositorioPerfilSocial repositorioPerfil,
                          RepositorioUsuario repositorioUsuario,
                          ApplicationEventPublisher eventPublisher) {
        this.repositorioPerfil = repositorioPerfil;
        this.repositorioUsuario = repositorioUsuario;
        this.eventPublisher = eventPublisher;
    }

    public void adicionarAmigo(UUID usuarioId, String emailAmigo) {
        PerfilSocial perfilUsuario = obterOuCriarPerfil(usuarioId);

        Usuario amigo = repositorioUsuario.buscarPorEmail(emailAmigo)
                .orElseThrow(() -> new IllegalArgumentException("Usuário com email " + emailAmigo + " não encontrado."));

        UUID amigoId = amigo.getId();

        if (usuarioId.equals(amigoId)) {
            throw new IllegalArgumentException("Você não pode adicionar a si mesmo.");
        }

        PerfilSocial perfilAmigo = obterOuCriarPerfil(amigoId);
        perfilUsuario.adicionarAmigo(amigoId);
        perfilAmigo.adicionarAmigo(usuarioId);

        repositorioPerfil.salvar(perfilUsuario);
        repositorioPerfil.salvar(perfilAmigo);
        eventPublisher.publishEvent(new AmizadeCriadaEvent(usuarioId, amigoId));
    }

    public void removerAmigo(UUID usuarioId, UUID amigoId) {
        PerfilSocial perfilUsuario = obterOuCriarPerfil(usuarioId);
        PerfilSocial perfilAmigo = obterOuCriarPerfil(amigoId);

        perfilUsuario.removerAmigo(amigoId);
        perfilAmigo.removerAmigo(usuarioId);

        repositorioPerfil.salvar(perfilUsuario);
        repositorioPerfil.salvar(perfilAmigo);
    }

    public List<AmigoDTO> listarAmigos(UUID usuarioId) {
        PerfilSocial perfilUsuario = obterOuCriarPerfil(usuarioId);

        Set<UUID> idsDosAmigos = perfilUsuario.getAmigos();
        return idsDosAmigos.stream()
                .map(id -> repositorioUsuario.buscarPorId(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(u -> new AmigoDTO(u.getId(), u.getNome(), u.getEmail()))
                .collect(Collectors.toList());
    }

    private PerfilSocial obterOuCriarPerfil(UUID usuarioId) {
        return repositorioPerfil.buscarPorUsuarioId(usuarioId)
                .orElseGet(() -> new PerfilSocial(usuarioId));
    }
}