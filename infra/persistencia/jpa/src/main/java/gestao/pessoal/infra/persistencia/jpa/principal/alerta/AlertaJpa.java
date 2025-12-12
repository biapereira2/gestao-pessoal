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
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private LocalDate dataDisparo;

    @Column(nullable = false)
    private boolean disparado;

    @Column(nullable = false)
    private String categoria;

    // Getters e setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDate getDataDisparo() { return dataDisparo; }
    public void setDataDisparo(LocalDate dataDisparo) { this.dataDisparo = dataDisparo; }

    public boolean isDisparado() { return disparado; }
    public void setDisparado(boolean disparado) { this.disparado = disparado; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
