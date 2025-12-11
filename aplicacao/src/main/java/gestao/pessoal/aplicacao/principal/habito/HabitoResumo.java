package gestao.pessoal.aplicacao.principal.habito;

import java.util.UUID;

public class HabitoResumo {

    private UUID id;
    private String nome;
    private String categoria;
    private String frequencia;

    public HabitoResumo(UUID id, String nome, String categoria, String frequencia) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.frequencia = frequencia;
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }
    public String getFrequencia() { return frequencia; }
}
