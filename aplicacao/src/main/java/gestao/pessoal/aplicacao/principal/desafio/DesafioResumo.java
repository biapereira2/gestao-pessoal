package gestao.pessoal.aplicacao.principal.desafio;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import gestao.pessoal.dominio.principal.princ.desafio.Desafio.StatusDesafio;

public class DesafioResumo {

    private final UUID id;
    private final String nome;
    private final LocalDate dataInicio;
    private final LocalDate dataFim;
    private final StatusDesafio status;
    private final int numParticipantes;

    public DesafioResumo(UUID id, String nome, LocalDate dataInicio, LocalDate dataFim, StatusDesafio status, int numParticipantes) {
        this.id = id;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
        this.numParticipantes = numParticipantes;
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public StatusDesafio getStatus() { return status; }
    public int getNumParticipantes() { return numParticipantes; }
}