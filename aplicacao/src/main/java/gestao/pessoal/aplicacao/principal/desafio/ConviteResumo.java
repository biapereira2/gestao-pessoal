package gestao.pessoal.aplicacao.principal.desafio;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConviteResumo {
    private final UUID conviteId;
    private final String nomeDesafio;
    private final String nomeCriador;
    private final LocalDateTime dataCriacao;

    public ConviteResumo(UUID conviteId, String nomeDesafio, String nomeCriador, LocalDateTime dataCriacao) {
        this.conviteId = conviteId;
        this.nomeDesafio = nomeDesafio;
        this.nomeCriador = nomeCriador;
        this.dataCriacao = dataCriacao;
    }

    public UUID getConviteId() { return conviteId; }
    public String getNomeDesafio() { return nomeDesafio; }
    public String getNomeCriador() { return nomeCriador; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
}