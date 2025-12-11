package gestao.pessoal.apresentacao.backend.principal.alerta;

import gestao.pessoal.dominio.principal.princ.alerta.Alerta;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class AlertaForm {

    @NotNull
    private UUID usuarioId;

    @NotNull
    private UUID metaId;

    @NotNull
    private Alerta.Condicao condicao;

    @NotNull
    private int valor;

    @NotNull
    private String descricao;

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public UUID getMetaId() { return metaId; }
    public void setMetaId(UUID metaId) { this.metaId = metaId; }

    public Alerta.Condicao getCondicao() { return condicao; }
    public void setCondicao(Alerta.Condicao condicao) { this.condicao = condicao; }

    public int getValor() { return valor; }
    public void setValor(int valor) { this.valor = valor; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
