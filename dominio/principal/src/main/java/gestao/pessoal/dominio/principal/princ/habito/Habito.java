package gestao.pessoal.dominio.principal.princ.habito;

import gestao.pessoal.dominio.principal.princ.habito.decorator.HabitoBase;

import java.util.UUID;

public class Habito implements HabitoBase {

    private UUID id;
    private UUID usuarioId;
    private String nome;
    private String descricao;
    private String categoria;
    private String frequencia;
    private int pontuacaoCheckin;

    public Habito() {}

    public Habito(UUID usuarioId, String nome, String descricao, String categoria, String frequencia) {
        if (nome == null || nome.trim().isEmpty())
            throw new IllegalArgumentException("O nome do hábito não pode ser vazio.");

        if (usuarioId == null)
            throw new IllegalArgumentException("Usuário inválido.");

        this.id = UUID.randomUUID();
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.frequencia = frequencia;
        this.pontuacaoCheckin = definirPontosPorFrequencia(frequencia);
    }

    public void atualizar(String nome, String descricao, String categoria, String frequencia) {
        if (nome == null || nome.trim().isEmpty())
            throw new IllegalArgumentException("O nome do hábito não pode ser vazio.");

        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.frequencia = frequencia;
        this.pontuacaoCheckin = definirPontosPorFrequencia(frequencia);
    }

    private int definirPontosPorFrequencia(String frequencia) {
        switch (frequencia.toLowerCase()) {
            case "diaria":
                return 10;
            case "semanal":
                return 100;
            case "mensal":
                return 500;
            default:
                throw new IllegalArgumentException("Frequência inválida");
        }
    }

    // 🔑 MÉTODOS DO DECORATOR
    @Override
    public int getPontos() {
        return pontuacaoCheckin;
    }

    @Override
    public String getNome() {
        return nome;
    }

    // getters e setters
    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public String getFrequencia() { return frequencia; }
}
