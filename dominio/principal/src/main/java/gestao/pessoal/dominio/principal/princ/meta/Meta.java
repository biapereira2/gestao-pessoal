package gestao.pessoal.dominio.principal.princ.meta;

import gestao.pessoal.dominio.principal.princ.meta.strategy.EstrategiaAlertaMeta;
import gestao.pessoal.dominio.principal.princ.meta.strategy.EstrategiaAlertaMetaDiaria;
import gestao.pessoal.dominio.principal.princ.meta.strategy.EstrategiaAlertaMetaMensal;
import gestao.pessoal.dominio.principal.princ.meta.strategy.EstrategiaAlertaMetaSemanal;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

public class Meta {

    public enum Tipo { DIARIA, SEMANAL, MENSAL }

    private final UUID id;
    private final UUID usuarioId;
    private final Tipo tipo;
    private final String descricao;

    private LocalDate prazo;
    private int quantidade;
    private int habitosCompletos;
    private boolean alertaProximoFalha;
    private List<UUID> habitosIds;

    // 🔹 Strategy
    private final EstrategiaAlertaMeta estrategiaAlerta;

    // Construtor vazio (necessário para frameworks)
    public Meta() {
        this.id = UUID.randomUUID();
        this.usuarioId = null;
        this.tipo = Tipo.DIARIA;
        this.descricao = "";
        this.quantidade = 1;
        this.habitosCompletos = 0;
        this.alertaProximoFalha = false;
        this.prazo = LocalDate.now().plusDays(1);
        this.estrategiaAlerta = new EstrategiaAlertaMetaDiaria();
    }

    // Construtor principal
    public Meta(UUID usuarioId, Tipo tipo, String descricao, int quantidade, List<UUID> habitosIds) {
        if (descricao == null || descricao.trim().isEmpty())
            throw new IllegalArgumentException("A descrição da meta não pode ser vazia.");

        if (quantidade <= 0)
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");

        if (tipo == null)
            throw new IllegalArgumentException("Tipo inválido.");

        if (usuarioId == null)
            throw new IllegalArgumentException("Usuário inválido.");

        if (habitosIds == null || habitosIds.isEmpty())
            throw new IllegalArgumentException("Uma meta deve estar associada a pelo menos um hábito.");

        this.id = UUID.randomUUID();
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.habitosCompletos = 0;
        this.alertaProximoFalha = false;
        this.habitosIds = habitosIds;

        this.prazo = LocalDate.now().plusDays(
                tipo == Tipo.SEMANAL ? 7 :
                        tipo == Tipo.MENSAL ? 30 : 1
        );

        // 🎯 Strategy definida pelo tipo da meta
        this.estrategiaAlerta = switch (tipo) {
            case DIARIA -> new EstrategiaAlertaMetaDiaria();
            case SEMANAL -> new EstrategiaAlertaMetaSemanal();
            case MENSAL -> new EstrategiaAlertaMetaMensal();
        };
    }

    // 🔔 Método com Strategy aplicada
    public void dispararAlertaSeNecessario() {
        alertaProximoFalha = estrategiaAlerta.deveDispararAlerta(this);

        if (alertaProximoFalha) {
            long diasRestantes = prazo != null
                    ? ChronoUnit.DAYS.between(LocalDate.now(), prazo)
                    : 0;

            int habitosRestantes = quantidade - habitosCompletos;

            System.out.println("⚠️ Atenção! Você está perto de falhar a meta ("
                    + tipo.toString().toLowerCase() + "): " + descricao
                    + ". Você tem " + diasRestantes + " dia(s) pra completar mais "
                    + habitosRestantes + " hábito(s).");
        }
    }

    // --- Getters e Setters ---
    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }
    public Tipo getTipo() { return tipo; }
    public String getDescricao() { return descricao; }

    public LocalDate getPrazo() { return prazo; }
    public void setPrazo(LocalDate prazo) { this.prazo = prazo; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public int getHabitosCompletos() { return habitosCompletos; }
    public void setHabitosCompletos(int habitosCompletos) { this.habitosCompletos = habitosCompletos; }

    public boolean isAlertaProximoFalha() { return alertaProximoFalha; }
    public void setAlertaProximoFalha(boolean alertaProximoFalha) {
        this.alertaProximoFalha = alertaProximoFalha;
    }

    public List<UUID> getHabitosIds() { return habitosIds; }
    public void setHabitosIds(List<UUID> habitosIds) { this.habitosIds = habitosIds; }
}
