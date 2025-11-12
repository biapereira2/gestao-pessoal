package gestao.pessoal.engajamento.badges;

import java.util.UUID;

public class Badges {

    public enum Categoria {
        NIVEL,
        META_ATINGIDA,
        HABITO_CONSECUTIVO
        // Adicionar outras categorias conforme o crescimento do sistema
    }

    private final UUID id;
    private final String nome;
    private final String descricao;
    private final Categoria categoria;
    private final int valorRequerido; // Nível, quantidade de metas, dias, etc.
    private final boolean desbloqueada;
    private final UUID usuarioId; // Se a badge for específica de um usuário (conquistada)

    // Construtor para Badges (Modelos/Disponíveis)
    public Badges(String nome, String descricao, Categoria categoria, int valorRequerido) {
        if (nome == null || nome.trim().isEmpty()) {
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

    // Construtor para Badges CONQUISTADAS por um Usuário
    private Badges(UUID id, String nome, String descricao, Categoria categoria, int valorRequerido, UUID usuarioId) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.valorRequerido = valorRequerido;
        this.desbloqueada = true;
        this.usuarioId = usuarioId;
    }

    public static Badges conceder(Badges modelo, UUID usuarioId) {
        return new Badges(modelo.id, modelo.nome, modelo.descricao, modelo.categoria, modelo.valorRequerido, usuarioId);
    }

    // --- Getters ---
    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public Categoria getCategoria() { return categoria; }
    public int getValorRequerido() { return valorRequerido; }
    public boolean isDesbloqueada() { return desbloqueada; }
    public UUID getUsuarioId() { return usuarioId; }
}