package gestao.pessoal.habito;

import java.time.LocalDate;
import java.util.UUID;

public class Alerta {

    public enum Condicao { FALTAM_DIAS }

    private final UUID id;
    private final UUID usuarioId;
    private final UUID metaId;
    private final Condicao condicao;
    private final int valor;
    private final String descricao;
    private boolean disparado;
    private final LocalDate dataCriacao;

    public Alerta(UUID usuarioId, UUID metaId, Condicao condicao, int valor, String descricao) {
        if (usuarioId == null) throw new IllegalArgumentException("Usuário inválido.");
        if (metaId == null) throw new IllegalArgumentException("Meta inválida.");
        if (condicao == null) throw new IllegalArgumentException("Condição inválida.");
        if (valor <= 0) throw new IllegalArgumentException("Valor inválido.");
        if (descricao == null || descricao.trim().isEmpty()) throw new IllegalArgumentException("Descrição obrigatória.");

        this.id = UUID.randomUUID();
        this.usuarioId = usuarioId;
        this.metaId = metaId;
        this.condicao = condicao;
        this.valor = valor;
        this.descricao = descricao;
        this.disparado = false;
        this.dataCriacao = LocalDate.now();
    }

    public boolean deveDisparar(LocalDate dataLimite) {
        if (condicao == Condicao.FALTAM_DIAS) {
            long diasRestantes = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dataLimite);
            return diasRestantes == valor;
        }
        return false;
    }

    public void marcarComoDisparado() {
        this.disparado = true;
    }

    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }
    public UUID getMetaId() { return metaId; }
    public Condicao getCondicao() { return condicao; }
    public int getValor() { return valor; }
    public void setValor(int novoValor) { }
    public String getDescricao() { return descricao; }
    public boolean isDisparado() { return disparado; }
}
