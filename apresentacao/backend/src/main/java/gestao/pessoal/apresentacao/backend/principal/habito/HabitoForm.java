package gestao.pessoal.apresentacao.backend.principal.habito;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class HabitoForm {

    @NotNull
    @Size(min = 1, max = 100)
    private String nome;

    private String descricao;
    private String categoria;

    @NotNull
    private String frequencia;

    @NotNull
    private UUID usuarioId;

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getFrequencia() { return frequencia; }
    public void setFrequencia(String frequencia) { this.frequencia = frequencia; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }
}
