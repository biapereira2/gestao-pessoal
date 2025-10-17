package gestao.pessoal.habito;

import java.util.UUID;

public class Habito {
    private final UUID id;
    private final UUID usuarioId;
    private String nome;
    private String descricao;
    private String categoria;
    private String frequencia;

    public Habito(UUID usuarioId, String nome, String descricao, String categoria, String frequencia) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do hábito não pode ser vazio.");
        }
        this.id = UUID.randomUUID();
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.frequencia = frequencia;
    }

    public void atualizar(String novoNome, String novaDescricao, String novaCategoria) {
        if (novoNome == null || novoNome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do hábito não pode ser vazio.");
        }
        this.nome = novoNome;
        this.descricao = novaDescricao;
        this.categoria = novaCategoria;
    }

    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; } // Retorna UUID
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public String getFrequencia() { return frequencia; }
}