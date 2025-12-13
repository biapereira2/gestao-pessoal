package gestao.pessoal.aplicacao.engajamento.badges;

import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioServiceApl;
import gestao.pessoal.dominio.principal.engajamento.badges.BadgesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BadgesServiceApl {

    private final BadgesRepositorioApl repositorio;
    private final BadgesService dominioService;
    private final UsuarioServiceApl usuarioService;

    public BadgesServiceApl(BadgesRepositorioApl repositorio,
                            BadgesService dominioService,
                            UsuarioServiceApl usuarioService) {
        this.repositorio = repositorio;
        this.dominioService = dominioService;
        this.usuarioService = usuarioService;
    }

    public void verificarEConceder(UUID usuarioId) {
        validarUsuario(usuarioId);
        dominioService.verificarEConcederBadges(usuarioId);
    }

    public List<BadgeResumo> listarConquistadas(UUID usuarioId) {
        validarUsuario(usuarioId);
        return repositorio.listarBadgesConquistadas(usuarioId);
    }

    public List<BadgeResumo> listarDisponiveis(UUID usuarioId) {
        validarUsuario(usuarioId);
        return repositorio.listarBadgesDisponiveis(usuarioId);
    }

    private void validarUsuario(UUID usuarioId) {
        if (usuarioService.buscarPorId(usuarioId).isEmpty()) {
            throw new IllegalArgumentException(
                    "Usuário com ID " + usuarioId + " não existe."
            );
        }
    }
}

