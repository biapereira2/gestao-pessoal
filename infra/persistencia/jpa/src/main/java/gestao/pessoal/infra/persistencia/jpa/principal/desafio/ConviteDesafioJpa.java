package gestao.pessoal.infra.persistencia.jpa.principal.desafio;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import gestao.pessoal.dominio.principal.princ.desafio.ConviteDesafio.StatusConvite;

@Entity
@Table(name = "convite_desafio")
public class ConviteDesafioJpa {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID desafioId;

    @Column(nullable = false)
    private UUID convidadoId;

    @Column(nullable = false)
    private UUID criadorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusConvite status;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    // Getters e Setters (Necessários para JPA)
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getDesafioId() { return desafioId; }
    public void setDesafioId(UUID desafioId) { this.desafioId = desafioId; }
    public UUID getConvidadoId() { return convidadoId; }
    public void setConvidadoId(UUID convidadoId) { this.convidadoId = convidadoId; }
    public UUID getCriadorId() { return criadorId; }
    public void setCriadorId(UUID criadorId) { this.criadorId = criadorId; }
    public StatusConvite getStatus() { return status; }
    public void setStatus(StatusConvite status) { this.status = status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}