package gestao.pessoal.apresentacao.backend.principal.alerta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public class AlertaForm {

    @NotNull
    private UUID usuarioId;

    @NotBlank
    private String titulo;

    private String descricao;

    @NotNull
    private LocalDate dataDisparo;

    private String categoria;

    // Getters e setters
    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDate getDataDisparo() { return dataDisparo; }
    public void setDataDisparo(LocalDate dataDisparo) { this.dataDisparo = dataDisparo; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
