package gestao.pessoal.infra.persistencia.jpa.principal.habito;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "habito")
public class HabitoJpa {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID usuarioId;

    @Column(nullable = false)
    private String nome;

    private String descricao;
    private String categoria;

    @Column(nullable = false)
    private String frequencia;

    @Column(nullable = false)
    private int pontuacaoCheckin;

    // Getters e setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getFrequencia() { return frequencia; }
    public void setFrequencia(String frequencia) { this.frequencia = frequencia; }

    public int getPontuacaoCheckin() { return pontuacaoCheckin; }
    public void setPontuacaoCheckin(int pontuacaoCheckin) { this.pontuacaoCheckin = pontuacaoCheckin; }
}
