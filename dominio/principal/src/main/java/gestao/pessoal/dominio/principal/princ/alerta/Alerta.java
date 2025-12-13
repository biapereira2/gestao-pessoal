package gestao.pessoal.dominio.principal.princ.alerta;

import gestao.pessoal.dominio.principal.princ.alerta.observer.AlertaObserver;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Alerta {

    private UUID id;
    private UUID usuarioId;
    private String titulo;
    private String descricao;
    private LocalDate dataDisparo;
    private boolean disparado;
    private String categoria;

    // Lista de observadores
    private final List<AlertaObserver> observers = new ArrayList<>();

    public Alerta(UUID usuarioId, String titulo, String descricao, LocalDate dataDisparo, String categoria) {
        if (usuarioId == null) throw new IllegalArgumentException("Usuário inválido.");
        if (titulo == null || titulo.isBlank()) throw new IllegalArgumentException("Título obrigatório.");
        if (dataDisparo == null) throw new IllegalArgumentException("Data de disparo obrigatória.");

        this.id = UUID.randomUUID();
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataDisparo = dataDisparo;
        this.disparado = false;
        this.categoria = categoria != null && !categoria.isBlank() ? categoria : "Geral";
    }

    public Alerta() {}

    // --- Observer methods ---
    public void adicionarObservador(AlertaObserver observer) {
        observers.add(observer);
    }

    public void removerObservador(AlertaObserver observer) {
        observers.remove(observer);
    }

    private void notificar() {
        for (AlertaObserver observer : observers) {
            observer.alterado(this);
        }
    }

    // --- Lógica de disparo ---
    public boolean deveDisparar() {
        return !disparado && !LocalDate.now().isBefore(dataDisparo);
    }

    public void marcarComoDisparado() {
        this.disparado = true;
        notificar(); // notifica os observadores quando disparado
    }

    // --- Getters e setters ---
    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDate getDataDisparo() { return dataDisparo; }
    public void setDataDisparo(LocalDate dataDisparo) { this.dataDisparo = dataDisparo; }
    public boolean isDisparado() { return disparado; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
