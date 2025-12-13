package gestao.pessoal.dominio.principal.engajamento.progressoUsuario;

import java.util.UUID;

public class ProgressoUsuario {

    private final UUID usuarioId;
    private int pontos;
    private int nivel;
    private int limiteProximoNivel;
    private int limiteNivelAtual;

    /* =========================
       CONSTRUTOR PRINCIPAL
       ========================= */
    public ProgressoUsuario(UUID usuarioId) {
        this.usuarioId = usuarioId;
        this.pontos = 0;
        this.nivel = 1;
        this.limiteNivelAtual = 0;
        this.limiteProximoNivel = 100;
    }

    /* =========================
       COMPORTAMENTO DE DOMÍNIO
       ========================= */

    public void adicionarPontos(int pontosGanhos) {
        if (pontosGanhos <= 0) {
            throw new IllegalArgumentException("Os pontos ganhos devem ser positivos.");
        }

        this.pontos += pontosGanhos;
        verificarNivel();
    }

    public void removerPontos(int pontosPerdidos) {
        if (pontosPerdidos <= 0) {
            throw new IllegalArgumentException("Os pontos removidos devem ser positivos.");
        }

        this.pontos = Math.max(0, this.pontos - pontosPerdidos);
        verificarNivel();
    }

    private void verificarNivel() {
        while (this.pontos >= this.limiteProximoNivel) {
            this.nivel++;
            atualizarLimites();
        }

        while (this.nivel > 1 && this.pontos < this.limiteNivelAtual) {
            this.nivel--;
            atualizarLimites();
        }
    }

    private void atualizarLimites() {
        int limiteAtual = 0;

        for (int i = 1; i < this.nivel; i++) {
            limiteAtual += i * 100;
        }

        this.limiteNivelAtual = limiteAtual;
        this.limiteProximoNivel = limiteAtual + (this.nivel * 100);
    }

    /* =========================
       MÉTODOS DE REIDRATAÇÃO
       (USO EXCLUSIVO DO REPOSITÓRIO)
       ========================= */

    public void definirEstadoPersistido(
            int pontos,
            int nivel,
            int limiteNivelAtual,
            int limiteProximoNivel
    ) {
        this.pontos = pontos;
        this.nivel = nivel;
        this.limiteNivelAtual = limiteNivelAtual;
        this.limiteProximoNivel = limiteProximoNivel;
    }

    /* =========================
       GETTERS
       ========================= */

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public int getPontos() {
        return pontos;
    }

    public int getNivel() {
        return nivel;
    }

    public int getLimiteNivelAtual() {
        return limiteNivelAtual;
    }

    public int getLimiteProximoNivel() {
        return limiteProximoNivel;
    }

    public int getPontosFaltantes() {
        return limiteProximoNivel - pontos;
    }
}
