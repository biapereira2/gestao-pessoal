package gestao.pessoal.dominio.principal.princ.rotina;

import java.util.List;
import java.util.UUID;

public class Rotina {
    private final UUID id;
    private final UUID usuarioId;
    private String nome;
    private String descricao;
    private List<UUID> habitosIds;

    public Rotina(UUID usuarioId, String nome, String descricao, List<UUID> habitosIds) {
        this.id = UUID.randomUUID();
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.descricao = descricao;
        this.habitosIds = habitosIds;
    }

    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public List<UUID> getHabitosIds() { return habitosIds; }

    public void atualizar(String novoNome, String novaDescricao, List<UUID> novosHabitos) {
        this.nome = novoNome;
        this.descricao = novaDescricao;
        this.habitosIds = novosHabitos;
    }
}
