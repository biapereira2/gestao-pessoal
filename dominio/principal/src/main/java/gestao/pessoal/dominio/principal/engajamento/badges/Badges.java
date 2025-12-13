package gestao.pessoal.dominio.principal.engajamento.badges;

import java.util.UUID;

public class Badges {

    public enum Categoria {
        NIVEL,
        META_ATINGIDA,
        HABITO_CONSECUTIVO
    }

    private final UUID id;
    private final String nome;
    private final String descricao;
    private final Categoria categoria;
    private final int valorRequerido;

    private final boolean desbloqueada;
    private final UUID usuarioId;

    // ============================
    // Construtor de CRIAÇÃO (modelo)
    // ============================
    public Badges(String nome, String descricao, Categoria categoria, int valorRequerido) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da badge não pode ser vazio.");
        }
        if (valorRequerido <= 0) {
            throw new IllegalArgumentException("O valor requerido deve ser maior que zero.");
        }

        this.id = UUID.randomUUID();
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.valorRequerido = valorRequerido;

        this.desbloqueada = false;
        this.usuarioId = null;
    }

    // ============================
    // Construtor de REIDRATAÇÃO
    // ============================
    private Badges(
            UUID id,
            String nome,
            String descricao,
            Categoria categoria,
            int valorRequerido,
            boolean desbloqueada,
            UUID usuarioId
    ) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.valorRequerido = valorRequerido;
        this.desbloqueada = desbloqueada;
        this.usuarioId = usuarioId;
    }

    // ============================
    // Fábricas explícitas
    // ============================

    public static Badges reidratarModelo(
            UUID id,
            String nome,
            String descricao,
            Categoria categoria,
            int valorRequerido
    ) {
        return new Badges(id, nome, descricao, categoria, valorRequerido, false, null);
    }

    public static Badges reidratarConquistada(
            UUID id,
            String nome,
            String descricao,
            Categoria categoria,
            int valorRequerido,
            UUID usuarioId
    ) {
        return new Badges(id, nome, descricao, categoria, valorRequerido, true, usuarioId);
    }

    // ============================
    // Comportamento de domínio
    // ============================

    public boolean pertenceAoUsuario(UUID usuarioId) {
        return desbloqueada && this.usuarioId.equals(usuarioId);
    }

    // ============================
    // Getters
    // ============================

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public Categoria getCategoria() { return categoria; }
    public int getValorRequerido() { return valorRequerido; }
    public boolean isDesbloqueada() { return desbloqueada; }
    public UUID getUsuarioId() { return usuarioId; }
}
