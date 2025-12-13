package gestao.pessoal.aplicacao.engajamento.progressoUsuario;

import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioServiceApl;
import gestao.pessoal.dominio.principal.engajamento.progressoUsuario.ProgressoUsuario;
import gestao.pessoal.dominio.principal.engajamento.progressoUsuario.ProgressoUsuarioService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProgressoUsuarioServiceApl {

    private final ProgressoUsuarioRepositorioApl repositorio;
    private final ProgressoUsuarioService dominioService;
    private final UsuarioServiceApl usuarioService;

    public ProgressoUsuarioServiceApl(ProgressoUsuarioRepositorioApl repositorio,
                                      ProgressoUsuarioService dominioService,
                                      UsuarioServiceApl usuarioService) {
        this.repositorio = repositorio;
        this.dominioService = dominioService;
        this.usuarioService = usuarioService;
    }

    public void adicionarPontos(UUID usuarioId, int pontos, String motivo) {
        validarUsuario(usuarioId);
        dominioService.adicionarPontos(usuarioId, pontos, motivo);
    }

    public void removerPontos(UUID usuarioId, int pontos, String motivo) {
        validarUsuario(usuarioId);
        dominioService.removerPontos(usuarioId, pontos, motivo);
    }

    public ProgressoUsuarioResumo visualizarResumo(UUID usuarioId) {
        validarUsuario(usuarioId);
        return repositorio.buscarResumoPorUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Progresso não encontrado para o usuário."
                ));
    }

    public ProgressoUsuario visualizarCompleto(UUID usuarioId) {
        validarUsuario(usuarioId);
        return repositorio.buscarPorUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Progresso não encontrado para o usuário."
                ));
    }

    private void validarUsuario(UUID usuarioId) {
        if (usuarioService.buscarPorId(usuarioId).isEmpty()) {
            throw new IllegalArgumentException(
                    "Usuário com ID " + usuarioId + " não existe."
            );
        }
    }
}

