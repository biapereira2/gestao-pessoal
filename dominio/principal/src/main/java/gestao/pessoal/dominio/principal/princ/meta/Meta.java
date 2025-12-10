package gestao.pessoal.dominio.principal.princ.meta;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    public Meta(UUID usuarioId, UUID habitoId, Tipo tipo, String descricao, int quantidade) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição da meta não pode ser vazia.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("tipo inválido");
        }
        if (usuarioId == null) {
            throw new IllegalArgumentException("Usuário inválido.");
        }

        this.id = UUID.randomUUID();
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.descricao = descricao;
        this.prazo = LocalDate.now().plusDays(tipo == Tipo.SEMANAL ? 7 : tipo == Tipo.MENSAL ? 30 : 1);
        this.quantidade = quantidade;
        this.habitosCompletos = 0;
        this.alertaProximoFalha = false;
    }

    public void atualizarQuantidade(int novaQuantidade) {
        if (novaQuantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        this.quantidade = novaQuantidade;
    }

    public void dispararAlertaSeNecessario() {
        double percentual = (double) habitosCompletos / quantidade;

        long diasRestantes = prazo != null ? ChronoUnit.DAYS.between(LocalDate.now(), prazo) : 0;
        boolean pertoDoPrazo = diasRestantes <= 2; // alerta se estiver a 2 dias ou menos do prazo

        switch (tipo) {
            case SEMANAL:
                alertaProximoFalha = percentual < 0.5 || pertoDoPrazo;
                break;
            case MENSAL:
                alertaProximoFalha = percentual < 0.25 || pertoDoPrazo;
                break;
            default:
                alertaProximoFalha = false;
        }

        if (alertaProximoFalha) {
            int habitosRestantes = quantidade - habitosCompletos;
            String tipoFormatado = tipo.toString().toLowerCase();

            System.out.println("⚠️ Atenção! Você está perto de falhar a meta ("
                    + tipoFormatado + "): " + descricao
                    + ". Você tem " + diasRestantes + " dia(s) pra completar mais "
                    + habitosRestantes + " hábito(s).");
        }
    }


    // --- Getters e Setters ---
    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }
    public String getDescricao() { return descricao; }
    public int getQuantidade() { return quantidade; }

    public Tipo getTipo() {
        return tipo;
    }

    public LocalDate getPrazo() {
        return prazo;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setAlertaProximoFalha(boolean alertaProximoFalha) {
        this.alertaProximoFalha = alertaProximoFalha;
    }

    public int getHabitosCompletos() {return habitosCompletos;}
    public void setHabitosCompletos(int habitosCompletos) { this.habitosCompletos = habitosCompletos; }
    public boolean isAlertaProximoFalha() { return alertaProximoFalha; }
    public void setPrazo(LocalDate prazo) { this.prazo = prazo; }
}
