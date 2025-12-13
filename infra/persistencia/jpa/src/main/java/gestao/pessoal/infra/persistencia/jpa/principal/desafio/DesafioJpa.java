package gestao.pessoal.infra.persistencia.jpa.principal.desafio;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import gestao.pessoal.dominio.principal.princ.desafio.Desafio.StatusDesafio;

@Entity
@Table(name = "desafio")
public class DesafioJpa {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID criadorId;

    @Column(nullable = false)
    private String nome;

    @ElementCollection // Para listar os IDs dos hábitos associados
    @CollectionTable(name = "desafio_habitos", joinColumns = @JoinColumn(name = "desafio_id"))
    @Column(name = "habito_id")
    private List<UUID> habitosIds;

    @ElementCollection // Para listar os IDs dos participantes
    @CollectionTable(name = "desafio_participantes", joinColumns = @JoinColumn(name = "desafio_id"))
    @Column(name = "participante_id")
    private List<UUID> participantesIds;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusDesafio status;

    // Getters e Setters (Necessários para JPA)
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getCriadorId() { return criadorId; }
    public void setCriadorId(UUID criadorId) { this.criadorId = criadorId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public List<UUID> getHabitosIds() { return habitosIds; }
    public void setHabitosIds(List<UUID> habitosIds) { this.habitosIds = habitosIds; }
    public List<UUID> getParticipantesIds() { return participantesIds; }
    public void setParticipantesIds(List<UUID> participantesIds) { this.participantesIds = participantesIds; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
    public StatusDesafio getStatus() { return status; }
    public void setStatus(StatusDesafio status) { this.status = status; }
}