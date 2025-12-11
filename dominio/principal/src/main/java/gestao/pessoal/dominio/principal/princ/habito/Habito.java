package gestao.pessoal.dominio.principal.princ.habito;

import java.util.UUID;

public class Habito {

    private UUID id;
    private UUID usuarioId;
    private String nome;
    private String descricao;
    private String categoria;
    private String frequencia;
    private int pontuacaoCheckin;

    // Construtor vazio necessário para ModelMapper / JPA
    public Habito() {
    }

    // Construtor principal para criação de hábitos
    public Habito(UUID usuarioId, String nome, String descricao, String categoria, String frequencia) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do hábito não pode ser vazio.");
        }
        if (usuarioId == null) {
            throw new IllegalArgumentException("Usuário inválido.");
        }

        this.id = UUID.randomUUID();
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.frequencia = frequencia;
        this.pontuacaoCheckin = definirPontosPorFrequencia(frequencia);
    }

    // Método para atualizar hábito
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

    // Cálculo automático de pontos por frequência
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

    // --- Getters e Setters ---
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
    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
        this.pontuacaoCheckin = definirPontosPorFrequencia(frequencia);
    }

    public int getPontos() { return pontuacaoCheckin; }
    public void setPontuacaoCheckin(int pontuacaoCheckin) { this.pontuacaoCheckin = pontuacaoCheckin; }
}
