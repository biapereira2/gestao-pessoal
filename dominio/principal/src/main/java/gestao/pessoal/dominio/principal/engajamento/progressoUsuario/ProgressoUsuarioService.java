package gestao.pessoal.dominio.principal.engajamento.progressoUsuario;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service de aplicação responsável por orquestrar
 * o progresso do usuário (pontos, nível, visualização).
 *
 * As regras de negócio permanecem nas entidades
 * (ProgressoUsuario).
 */
@Service
public class ProgressoUsuarioService {

    private final RepositorioProgressoUsuario repositorioProgresso;

    public ProgressoUsuarioService(RepositorioProgressoUsuario repositorioProgresso) {
        this.repositorioProgresso = repositorioProgresso;
    }

    // Cenário 1 – Ganhar pontos
    public void adicionarPontos(UUID usuarioId, int pontos, String motivo) {
        ProgressoUsuario progresso = obterOuCriarProgresso(usuarioId);

        // Em um sistema real, o "motivo" viraria um HistóricoPontuacao
        progresso.adicionarPontos(pontos);

        repositorioProgresso.salvar(progresso);
    }

    // Cenário 2 – Retirar pontos
    public void removerPontos(UUID usuarioId, int pontos, String motivo) {
        ProgressoUsuario progresso = repositorioProgresso.buscarPorUsuarioId(usuarioId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Progresso não encontrado para o usuário."));

        progresso.removerPontos(pontos);
        repositorioProgresso.salvar(progresso);
    }

    // Cenário 4 – Visualizar progresso
    public ProgressoUsuario visualizarProgresso(UUID usuarioId) {
        return repositorioProgresso.buscarPorUsuarioId(usuarioId)
                .orElseGet(() -> {
                    ProgressoUsuario novo = new ProgressoUsuario(usuarioId);
                    repositorioProgresso.salvar(novo);
                    return novo;
                });
    }

    // --- MÉTODOS INTERNOS ---

    private ProgressoUsuario obterOuCriarProgresso(UUID usuarioId) {
        return repositorioProgresso.buscarPorUsuarioId(usuarioId)
                .orElseGet(() -> {
                    ProgressoUsuario novo = new ProgressoUsuario(usuarioId);
                    repositorioProgresso.salvar(novo);
                    return novo;
                });
    }
}
