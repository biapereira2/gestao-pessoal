package gestao.pessoal.dominio.principal.princ.desafio;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Desafio {
    private final UUID id;
    private final UUID criadorId;
    private final String nome;
    private final List<UUID> habitosIds;
    private List<UUID> participantesIds; // Inclui o criador
    private final LocalDate dataInicio;
    private final LocalDate dataFim;
    private StatusDesafio status;

    public Desafio(UUID id, UUID criadorId, String nome, List<UUID> habitosIds, LocalDate dataInicio, LocalDate dataFim) {
        this.id = id;
        this.criadorId = criadorId;
        this.nome = nome;
        this.habitosIds = habitosIds;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = StatusDesafio.ATIVO;
        this.participantesIds = new java.util.ArrayList<>(List.of(criadorId)); // Criador é o primeiro participante
    }
    // Método para adicionar participante após aceitação de convite
    public void adicionarParticipante(UUID participanteId) {
        if (!participantesIds.contains(participanteId)) {
            participantesIds.add(participanteId);
        }
    }

    // Método para remover participante (saída do desafio)
    public void removerParticipante(UUID participanteId) {
        participantesIds.remove(participanteId);
    }

    // Método para encerrar o desafio
    public void encerrar() {
        this.status = StatusDesafio.ENCERRADO;
    }

    // Getters

    public UUID getId() {
        return id;
    }

    public UUID getCriadorId() {
        return criadorId;
    }

    public String getNome() {
        return nome;
    }

    public List<UUID> getHabitosIds() {
        return habitosIds;
    }

    public List<UUID> getParticipantesIds() {
        return participantesIds;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public StatusDesafio getStatus() {
        return status;
    }

    public enum StatusDesafio {
        ATIVO, ENCERRADO, CANCELADO
    }
}