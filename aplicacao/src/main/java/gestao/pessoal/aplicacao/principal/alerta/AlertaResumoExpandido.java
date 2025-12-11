package gestao.pessoal.aplicacao.principal.alerta;

import java.time.LocalDate;
import java.util.UUID;

public class AlertaResumoExpandido {

    private UUID id;
    private UUID usuarioId;
    private UUID metaId;
    private String condicao;
    private int valor;
    private String descricao;
    private boolean disparado;
    private LocalDate dataCriacao;

    public AlertaResumoExpandido(UUID id, UUID usuarioId, UUID metaId, String condicao,
                                 int valor, String descricao, boolean disparado, LocalDate dataCriacao) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.metaId = metaId;
        this.condicao = condicao;
        this.valor = valor;
        this.descricao = descricao;
        this.disparado = disparado;
        this.dataCriacao = dataCriacao;
    }

    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }
    public UUID getMetaId() { return metaId; }
    public String getCondicao() { return condicao; }
    public int getValor() { return valor; }
    public String getDescricao() { return descricao; }
    public boolean isDisparado() { return disparado; }
    public LocalDate getDataCriacao() { return dataCriacao; }
}
