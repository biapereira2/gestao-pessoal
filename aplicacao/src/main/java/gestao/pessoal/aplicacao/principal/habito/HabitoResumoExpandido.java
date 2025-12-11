package gestao.pessoal.aplicacao.principal.habito;

import java.util.UUID;

public class HabitoResumoExpandido {

    private UUID id;
    private String nome;
    private String descricao;
    private String categoria;
    private String frequencia;
    private int pontuacaoCheckin;

    public HabitoResumoExpandido(UUID id, String nome, String descricao,
                                 String categoria, String frequencia, int pontuacaoCheckin) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.frequencia = frequencia;
        this.pontuacaoCheckin = pontuacaoCheckin;
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public String getFrequencia() { return frequencia; }
    public int getPontuacaoCheckin() { return pontuacaoCheckin; }
}
