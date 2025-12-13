package gestao.pessoal.aplicacao.engajamento.progressoUsuario;

import java.util.UUID;

public class ProgressoUsuarioResumo {

    private UUID usuarioId;
    private int pontos;
    private int nivel;
    private int limiteNivelAtual;
    private int limiteProximoNivel;
    private int pontosFaltantes;

    public ProgressoUsuarioResumo(UUID usuarioId, int pontos, int nivel,
                                  int limiteNivelAtual, int limiteProximoNivel,
                                  int pontosFaltantes) {
        this.usuarioId = usuarioId;
        this.pontos = pontos;
        this.nivel = nivel;
        this.limiteNivelAtual = limiteNivelAtual;
        this.limiteProximoNivel = limiteProximoNivel;
        this.pontosFaltantes = pontosFaltantes;
    }

    public UUID getUsuarioId() { return usuarioId; }
    public int getPontos() { return pontos; }
    public int getNivel() { return nivel; }
    public int getLimiteNivelAtual() { return limiteNivelAtual; }
    public int getLimiteProximoNivel() { return limiteProximoNivel; }
    public int getPontosFaltantes() { return pontosFaltantes; }
}

