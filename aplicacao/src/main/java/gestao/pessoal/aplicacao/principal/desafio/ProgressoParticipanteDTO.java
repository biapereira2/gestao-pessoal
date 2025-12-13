package gestao.pessoal.aplicacao.principal.desafio;

import java.util.UUID;

public class ProgressoParticipanteDTO {
    private final UUID participanteId;
    private final String nomeParticipante;
    private final int habitosConcluidos;
    private final int totalHabitos;
    private final double porcentagemConclusao;

    public ProgressoParticipanteDTO(UUID participanteId, String nomeParticipante, int habitosConcluidos, int totalHabitos) {
        this.participanteId = participanteId;
        this.nomeParticipante = nomeParticipante;
        this.habitosConcluidos = habitosConcluidos;
        this.totalHabitos = totalHabitos;
        this.porcentagemConclusao = totalHabitos > 0 ? ((double) habitosConcluidos / totalHabitos) * 100 : 0;
    }

    public UUID getParticipanteId() { return participanteId; }
    public String getNomeParticipante() { return nomeParticipante; }
    public int getHabitosConcluidos() { return habitosConcluidos; }
    public int getTotalHabitos() { return totalHabitos; }
    public double getPorcentagemConclusao() { return porcentagemConclusao; }
}