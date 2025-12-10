package gestao.pessoal.dominio.principal.engajamento.progressoUsuario;

import java.util.UUID;

public class ProgressoUsuario {
    private final UUID usuarioId;
    private int pontos;
    private int nivel;
    private int limiteProximoNivel;
    private int limiteNivelAtual;

    public ProgressoUsuario(UUID usuarioId) {
        this.usuarioId = usuarioId;
        this.pontos = 0;
        this.nivel = 1;
        this.limiteProximoNivel = 100;
        this.limiteNivelAtual = 0;
    }

    public void adicionarPontos(int pontosGanhos) {
        if (pontosGanhos < 0) throw new IllegalArgumentException("Os pontos ganhos devem ser positivos.");
        this.pontos += pontosGanhos;
        verificarNivel();
    }

    public void removerPontos(int pontosPerdidos) {
        if (pontosPerdidos < 0) throw new IllegalArgumentException("Os pontos removidos devem ser positivos.");
        this.pontos = Math.max(0, this.pontos - pontosPerdidos);
        verificarNivel();
    }

    public void verificarNivel() {
        while (this.pontos >= this.limiteProximoNivel) {
            this.nivel++;
            this.limiteProximoNivel += this.nivel * 100;
            setLimiteNivelAtual();
        }

        while (this.pontos < this.limiteNivelAtual) {
            this.nivel--;
            this.limiteProximoNivel = limiteNivelAtual;
            setLimiteNivelAtual();
        }
    }

    public void setLimiteNivelAtual(){
        int limiteNivelAtual = 0;

        for (int i=1; i<this.nivel; i++){
            limiteNivelAtual += i;
        }

        limiteNivelAtual *= 100;

        this.limiteNivelAtual = limiteNivelAtual;
    }

    public int getLimiteNivelAtual(){ return this.limiteNivelAtual; }
    public int getPontos() { return this.pontos; }
    public int getNivel() { return this.nivel; }
    public int getLimiteProximoNivel() { return this.limiteProximoNivel; }
    public UUID getUsuarioId() { return this.usuarioId; }

    public int getPontosFaltantes() {
        return limiteProximoNivel - pontos;
    }
}