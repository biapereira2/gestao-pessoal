package gestao.pessoal.dominio.principal.princ.meta;

import gestao.pessoal.dominio.principal.compartilhado.usuario.RepositorioUsuario;
import gestao.pessoal.dominio.principal.princ.habito.RepositorioHabito;

import java.util.List;
import java.util.UUID;

public class MetaService {

    private final RepositorioMeta repositorioMeta;
    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioHabito repositorioHabito;

    public MetaService(RepositorioMeta repositorioMeta, RepositorioUsuario repositorioUsuario, RepositorioHabito repositorioHabito) {
        this.repositorioMeta = repositorioMeta;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioHabito = repositorioHabito;
    }

    // --- Criar meta ---
    public void criar(UUID usuarioId, List<UUID> habitosIds, Meta.Tipo tipo, String descricao) {
        if (tipo == null)
            throw new IllegalArgumentException("Tipo inválido");

        if (habitosIds == null || habitosIds.isEmpty())
            throw new IllegalArgumentException("É necessário associar pelo menos um hábito à meta.");

        if (repositorioUsuario.buscarPorId(usuarioId).isEmpty())
            throw new IllegalArgumentException("Usuário não encontrado.");

        // Verifica se todos os hábitos existem
        for (UUID habitoId : habitosIds) {
            if (habitoId == null || repositorioHabito.buscarPorId(habitoId).isEmpty()) {
                throw new IllegalArgumentException("Hábito não encontrado: " + habitoId);
            }
        }

        // Cria a meta com a lista de IDs de Hábitos.
        Meta meta = new Meta(usuarioId, tipo, descricao, habitosIds.size(), habitosIds);
        repositorioMeta.salvar(meta);
    }




    // --- Atualizar meta ---
    public void atualizar(UUID metaId, int novaQuantidade) {
        Meta meta = repositorioMeta.buscarPorId(metaId)
                .orElseThrow(() -> new IllegalArgumentException("Meta com ID " + metaId + " não encontrada."));
        meta.atualizarQuantidade(novaQuantidade);
        repositorioMeta.salvar(meta);
    }

    // NOVO: Adiciona a possibilidade de atualizar os hábitos associados
    public void atualizarHabitos(UUID metaId, List<UUID> novosHabitosIds) {
        Meta meta = repositorioMeta.buscarPorId(metaId)
                .orElseThrow(() -> new IllegalArgumentException("Meta com ID " + metaId + " não encontrada."));

        if (novosHabitosIds == null || novosHabitosIds.isEmpty())
            throw new IllegalArgumentException("A meta deve ter pelo menos um hábito.");

        // Validação se os novos hábitos existem
        for (UUID habitoId : novosHabitosIds) {
            if (habitoId == null || repositorioHabito.buscarPorId(habitoId).isEmpty()) {
                throw new IllegalArgumentException("Hábito não encontrado: " + habitoId);
            }
        }

        // Atualiza a lista de IDs e a quantidade
        meta.setHabitosIds(novosHabitosIds);
        meta.setQuantidade(novosHabitosIds.size());

        repositorioMeta.salvar(meta);
    }

    // --- Excluir meta ---
    public void excluir(UUID metaId) {
        repositorioMeta.remover(metaId);
    }

    // --- Verificar alerta de uma meta específica ---
    public void verificarAlerta(UUID metaId, String periodo) {
        Meta meta = repositorioMeta.buscarPorId(metaId)
                .orElseThrow(() -> new IllegalArgumentException("Meta com ID " + metaId + " não encontrada."));

        // O próprio método da Meta agora imprime a mensagem detalhada
        meta.dispararAlertaSeNecessario();
    }

}