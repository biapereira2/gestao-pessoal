package gestao.pessoal.aplicacao;

import gestao.pessoal.engajamento.ProgressoUsuario;
import gestao.pessoal.engajamento.RepositorioProgressoUsuario;

import java.util.UUID;

public class ProgressoUsuarioService {

    private final RepositorioProgressoUsuario repositorioProgresso;

    public ProgressoUsuarioService(RepositorioProgressoUsuario repositorioProgresso) {
        this.repositorioProgresso = repositorioProgresso;
    }

    // Cenário 1 – Ganhar pontos
    public void adicionarPontos(UUID usuarioId, int pontos) {
        ProgressoUsuario progresso = obterOuCriarProgresso(usuarioId);
        progresso.adicionarPontos(pontos);
        repositorioProgresso.salvar(progresso);
    }

    // Cenário 2 – Retirar pontos
    public void removerPontos(UUID usuarioId, int pontos) {
        ProgressoUsuario progresso = repositorioProgresso.buscarPorUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Progresso não encontrado para o usuário."));
        progresso.removerPontos(pontos);
        repositorioProgresso.salvar(progresso);
    }

    // Cenário 3 – Subir de nível (verificado automaticamente dentro da entidade)

    // Cenário 4 – Visualizar progresso
    public ProgressoUsuario visualizarProgresso(UUID usuarioId) {
        return repositorioProgresso.buscarPorUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Progresso não encontrado para o usuário."));
    }

    private ProgressoUsuario obterOuCriarProgresso(UUID usuarioId) {
        return repositorioProgresso.buscarPorUsuarioId(usuarioId)
                .orElseGet(() -> {
                    ProgressoUsuario novo = new ProgressoUsuario(usuarioId);
                    repositorioProgresso.salvar(novo);
                    return novo;
                });
    }
}
