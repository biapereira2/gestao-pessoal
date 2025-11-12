package gestao.pessoal.principal.habito;

import java.util.UUID;

public class Habito {
    private final UUID id;
    private final UUID usuarioId;
    private String nome;
    private String descricao;
    private String categoria;
    private String frequencia;
    private int pontuacaoCheckin;

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
        this.pontuacaoCheckin = definirPontosPorFrequencia(frequencia); // <-- calculo automatico
    }

    private int definirPontosPorFrequencia(String frequencia) {
        if (frequencia == null) {
            throw new IllegalArgumentException("A frequência não pode ser nula.");
        }

        switch (frequencia.toLowerCase()) {
            case "diaria":
                return 10;
            case "semanal":
                return 100;
            case "mensal":
                return 500;
            default:
                throw new IllegalArgumentException("Frequência inválida: " + frequencia);
        }
    }

    public void atualizar(String novoNome, String novaDescricao, String novaCategoria, String novaFrequencia) {
        if (novoNome == null || novoNome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do hábito não pode ser vazio.");
        }

        this.nome = novoNome;
        this.descricao = novaDescricao;
        this.categoria = novaCategoria;

        if (novaFrequencia != null && !novaFrequencia.equalsIgnoreCase(this.frequencia)) {
            this.frequencia = novaFrequencia;
            this.pontuacaoCheckin = definirPontosPorFrequencia(novaFrequencia);
        }
    }

    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public String getFrequencia() { return frequencia; }
    public int getPontos() { return pontuacaoCheckin; }
}
