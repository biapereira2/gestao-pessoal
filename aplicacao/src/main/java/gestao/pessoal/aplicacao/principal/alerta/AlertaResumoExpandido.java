package gestao.pessoal.aplicacao.principal.alerta;

import java.time.LocalDate;
import java.util.UUID;

public class AlertaResumoExpandido {

    private UUID id;
    private UUID usuarioId;
    private String titulo;
    private String descricao;
    private LocalDate dataDisparo;
    private boolean disparado;
    private String categoria;

    public AlertaResumoExpandido(UUID id, UUID usuarioId, String titulo, String descricao,
                                 LocalDate dataDisparo, boolean disparado, String categoria) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataDisparo = dataDisparo;
        this.disparado = disparado;
        this.categoria = categoria;
    }

    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public LocalDate getDataDisparo() { return dataDisparo; }
    public boolean isDisparado() { return disparado; }
    public String getCategoria() { return categoria; }
}
