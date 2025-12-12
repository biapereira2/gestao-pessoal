package gestao.pessoal.aplicacao.principal.alerta;

import java.util.UUID;

public class AlertaResumo {

    private UUID id;
    private String titulo;
    private String descricao;
    private boolean disparado;
    private String categoria;

    public AlertaResumo(UUID id, String titulo, String descricao, boolean disparado, String categoria) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.disparado = disparado;
        this.categoria = categoria;
    }

    public UUID getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public boolean isDisparado() { return disparado; }
    public String getCategoria() { return categoria; }
}
