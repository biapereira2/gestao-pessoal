package gestao.pessoal.aplicacao;

import gestao.pessoal.compartilhado.RepositorioUsuario;
import gestao.pessoal.habito.Meta;
import gestao.pessoal.habito.RepositorioMeta;
import java.util.UUID;

public class MetaService {

    private final RepositorioMeta repositorioMeta;
    private final RepositorioUsuario repositorioUsuario;

    public MetaService(RepositorioMeta repositorioMeta, RepositorioUsuario repositorioUsuario) {
        this.repositorioMeta = repositorioMeta;
        this.repositorioUsuario = repositorioUsuario;
    }

    // --- Criar meta ---
    public void criar(UUID usuarioId, UUID habitoId, Meta.Tipo tipo, String descricao, int quantidade) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo inválido");
        }
        if (repositorioUsuario.buscarPorId(usuarioId).isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        Meta meta = new Meta(usuarioId, habitoId, tipo, descricao, quantidade);
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
    public void verificarAlerta(UUID metaId, String periodo) {
        Meta meta = repositorioMeta.buscarPorId(metaId)
                .orElseThrow(() -> new IllegalArgumentException("Meta com ID " + metaId + " não encontrada."));

        // Atualiza o flag de alerta baseado na quantidade de hábitos completados
        meta.dispararAlertaSeNecessario();

        if (meta.isAlertaProximoFalha()) {
            System.out.println("⚠️ Atenção! Você está perto de falhar a meta: " + meta.getDescricao());
        }
    }
}
