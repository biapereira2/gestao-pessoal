package gestao.pessoal.infra.persistencia.jpa.principal.alerta;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "alerta")
public class AlertaJpa {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID usuarioId;

    @Column(nullable = false)
    private UUID metaId;

    @Column(nullable = false)
    private String condicao;

    @Column(nullable = false)
    private int valor;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private boolean disparado;

    @Column(nullable = false)
    private LocalDate dataCriacao;

    // Getters e setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public UUID getMetaId() { return metaId; }
    public void setMetaId(UUID metaId) { this.metaId = metaId; }

    public String getCondicao() { return condicao; }
    public void setCondicao(String condicao) { this.condicao = condicao; }

    public int getValor() { return valor; }
    public void setValor(int valor) { this.valor = valor; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public boolean isDisparado() { return disparado; }
    public void setDisparado(boolean disparado) { this.disparado = disparado; }

    public LocalDate getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }
}
