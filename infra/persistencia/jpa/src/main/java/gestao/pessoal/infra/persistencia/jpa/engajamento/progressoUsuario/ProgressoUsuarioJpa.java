package gestao.pessoal.infra.persistencia.jpa.engajamento.progressoUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "PROGRESSO_USUARIO")
public class ProgressoUsuarioJpa {

    @Id
    private UUID usuarioId;

    @Column(nullable = false)
    private int pontos;

    @Column(nullable = false)
    private int nivel;

    @Column(nullable = false)
    private int limiteProximoNivel;

    @Column(nullable = false)
    private int limiteNivelAtual;

    @Column(nullable = false)
    private int pontosFaltantes;

    /* getters e setters */

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getLimiteProximoNivel() {
        return limiteProximoNivel;
    }

    public void setLimiteProximoNivel(int limiteProximoNivel) {
        this.limiteProximoNivel = limiteProximoNivel;
    }

    public int getLimiteNivelAtual() {
        return limiteNivelAtual;
    }

    public void setLimiteNivelAtual(int limiteNivelAtual) {
        this.limiteNivelAtual = limiteNivelAtual;
    }

    public int getPontosFaltantes() {
        return limiteNivelAtual - pontos;
    }

    public void setPontosFaltantes(int pontosFaltantes) {
        this.pontosFaltantes = pontosFaltantes;
    }
}

