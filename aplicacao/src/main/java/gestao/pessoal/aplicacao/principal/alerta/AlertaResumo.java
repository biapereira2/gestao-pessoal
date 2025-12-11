package gestao.pessoal.aplicacao.principal.alerta;

import java.util.UUID;

public class AlertaResumo {

    private UUID id;
    private String descricao;
    private String condicao;
    private int valor;
    private boolean disparado;

    public AlertaResumo(UUID id, String descricao, String condicao, int valor, boolean disparado) {
        this.id = id;
        this.descricao = descricao;
        this.condicao = condicao;
        this.valor = valor;
        this.disparado = disparado;
    }

    public UUID getId() { return id; }
    public String getDescricao() { return descricao; }
    public String getCondicao() { return condicao; }
    public int getValor() { return valor; }
    public boolean isDisparado() { return disparado; }
}
