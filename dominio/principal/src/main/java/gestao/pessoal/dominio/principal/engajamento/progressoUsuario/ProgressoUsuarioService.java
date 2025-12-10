package gestao.pessoal.dominio.principal.engajamento.progressoUsuario;

import java.util.UUID;

public class ProgressoUsuarioService {

    private final RepositorioProgressoUsuario repositorioProgresso;

    public ProgressoUsuarioService(RepositorioProgressoUsuario repositorioProgresso) {
        this.repositorioProgresso = repositorioProgresso;
    }

    // Cenário 1 – Ganhar pontos (AGORA COM PARÂMETRO 'MOTIVO')
    public void adicionarPontos(UUID usuarioId, int pontos, String motivo) {
        ProgressoUsuario progresso = obterOuCriarProgresso(usuarioId);

        // TODO: Em um sistema real, você registraria o "motivo" em um objeto HistóricoPontuacao

        progresso.adicionarPontos(pontos);
        repositorioProgresso.salvar(progresso);
    }

    // Cenário 2 – Retirar pontos (AGORA COM PARÂMETRO 'MOTIVO')
    public void removerPontos(UUID usuarioId, int pontos, String motivo) {
        ProgressoUsuario progresso = repositorioProgresso.buscarPorUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Progresso não encontrado para o usuário."));

        // TODO: Em um sistema real, você registraria o "motivo" em um objeto HistóricoPontuacao

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