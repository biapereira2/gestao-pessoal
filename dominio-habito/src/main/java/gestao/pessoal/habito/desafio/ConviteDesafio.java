package gestao.pessoal.habito;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConviteDesafio {
    private final UUID id;
    private final UUID desafioId;
    private final UUID convidadoId;
    private final UUID criadorId;
    private StatusConvite status;
    private final LocalDateTime dataCriacao;

    public ConviteDesafio(UUID id, UUID desafioId, UUID convidadoId, UUID criadorId) {
        this.id = id;
        this.desafioId = desafioId;
        this.convidadoId = convidadoId;
        this.criadorId = criadorId;
        this.status = StatusConvite.PENDENTE;
        this.dataCriacao = LocalDateTime.now();
    }

    public void aceitar() {
        this.status = StatusConvite.ACEITO;
    }

    public void rejeitar() {
        this.status = StatusConvite.REJEITADO;
    }

    public UUID getId() {
        return id;
    }

    public UUID getDesafioId() {
        return desafioId;
    }

    public UUID getConvidadoId() {
        return convidadoId;
    }

    public UUID getCriadorId() {
        return criadorId;
    }

    public StatusConvite getStatus() {
        return status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public enum StatusConvite {
        PENDENTE, ACEITO, REJEITADO
    }
}