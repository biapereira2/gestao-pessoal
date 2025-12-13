package gestao.pessoal.aplicacao.engajamento.badges;

import gestao.pessoal.dominio.principal.engajamento.badges.Badges;

import java.util.UUID;

public class BadgeResumo {

    private UUID id;
    private String nome;
    private String descricao;
    private Badges.Categoria categoria;
    private int valorRequerido;
    private boolean desbloqueada;

    public BadgeResumo(UUID id, String nome, String descricao,
                       Badges.Categoria categoria, int valorRequerido,
                       boolean desbloqueada) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.valorRequerido = valorRequerido;
        this.desbloqueada = desbloqueada;
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public Badges.Categoria getCategoria() { return categoria; }
    public int getValorRequerido() { return valorRequerido; }
    public boolean isDesbloqueada() { return desbloqueada; }
}

