package gestao.pessoal.infra.persistencia.jpa.engajamento.badges;

import gestao.pessoal.dominio.principal.engajamento.badges.Badges;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "BADGE_MODELO")
public class BadgeModeloJpa {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Badges.Categoria categoria;

    @Column(nullable = false)
    private int valorRequerido;

    // getters e setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Badges.Categoria getCategoria() { return categoria; }
    public void setCategoria(Badges.Categoria categoria) { this.categoria = categoria; }

    public int getValorRequerido() { return valorRequerido; }
    public void setValorRequerido(int valorRequerido) { this.valorRequerido = valorRequerido; }
}

