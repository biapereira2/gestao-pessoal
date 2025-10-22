package gestao.pessoal.aplicacao;

import gestao.pessoal.compartilhado.RepositorioUsuario;
import gestao.pessoal.habito.Meta;
import gestao.pessoal.habito.RepositorioHabito;
import gestao.pessoal.habito.RepositorioMeta;

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

        if (repositorioUsuario.buscarPorId(usuarioId).isEmpty())
            throw new IllegalArgumentException("Usuário não encontrado.");

        // Verifica se todos os hábitos existem
        for (UUID habitoId : habitosIds) {
            if (habitoId == null || repositorioHabito.buscarPorId(habitoId).isEmpty()) {
                throw new IllegalArgumentException("Hábito não encontrado: " + habitoId);
            }
        }

        // Cria apenas UMA meta para o conjunto de hábitos
        Meta meta = new Meta(usuarioId, null, tipo, descricao, habitosIds.size());
        repositorioMeta.salvar(meta);
    }




    // --- Atualizar meta ---
    public void atualizar(UUID metaId, int novaQuantidade) {
        Meta meta = repositorioMeta.buscarPorId(metaId)
                .orElseThrow(() -> new IllegalArgumentException("Meta com ID " + metaId + " não encontrada."));
        meta.atualizarQuantidade(novaQuantidade);
        repositorioMeta.salvar(meta);
    }

    // --- Excluir meta ---
    public void excluir(UUID metaId) {
        repositorioMeta.remover(metaId);
    }

    // --- Verificar alerta de uma meta específica ---
    // --- Verificar alerta de uma meta específica ---
    public void verificarAlerta(UUID metaId, String periodo) {
        Meta meta = repositorioMeta.buscarPorId(metaId)
                .orElseThrow(() -> new IllegalArgumentException("Meta com ID " + metaId + " não encontrada."));

        // O próprio método da Meta agora imprime a mensagem detalhada
        meta.dispararAlertaSeNecessario();
    }

}
