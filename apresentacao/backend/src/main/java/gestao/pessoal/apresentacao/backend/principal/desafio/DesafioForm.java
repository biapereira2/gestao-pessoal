package gestao.pessoal.apresentacao.backend.principal.desafio;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class DesafioForm {

    @NotNull private String nome;

    @NotNull private LocalDate dataFim;

    @NotNull private UUID criadorId;

    private List<UUID> habitosIds; // Pode ser nulo se o desafio for genérico

    private List<String> emailsConvidados; // Lista de emails

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public UUID getCriadorId() { return criadorId; }
    public void setCriadorId(UUID criadorId) { this.criadorId = criadorId; }

    public List<UUID> getHabitosIds() { return habitosIds; }
    public void setHabitosIds(List<UUID> habitosIds) { this.habitosIds = habitosIds; }

    public List<String> getEmailsConvidados() { return emailsConvidados; }
    public void setEmailsConvidados(List<String> emailsConvidados) { this.emailsConvidados = emailsConvidados; }
}