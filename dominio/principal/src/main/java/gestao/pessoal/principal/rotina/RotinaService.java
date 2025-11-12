package gestao.pessoal.principal.rotina;

import java.util.List;
import java.util.UUID;

public class RotinaService {

    private final RepositorioRotina repositorioRotina;

    public RotinaService(RepositorioRotina repositorioRotina) {
        this.repositorioRotina = repositorioRotina;
    }

    public Rotina criarRotina(UUID usuarioId, String nome, String descricao, List<UUID> habitosIds) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da rotina não pode ser vazio.");
        }

        boolean existe = repositorioRotina.listarTodosPorUsuario(usuarioId)
                .stream()
                .anyMatch(r -> r.getNome().equalsIgnoreCase(nome));
        if (existe) {
            throw new IllegalArgumentException("A rotina já existe para este usuário.");
        }

        Rotina rotina = new Rotina(usuarioId, nome, descricao, habitosIds);
        repositorioRotina.salvar(rotina);
        return rotina;
    }

    public void atualizarRotina(UUID rotinaId, String novoNome, String novaDescricao, List<UUID> novosHabitos) {
        Rotina rotina = repositorioRotina.buscarPorId(rotinaId)
                .orElseThrow(() -> new IllegalArgumentException("Rotina com ID " + rotinaId + " não encontrada."));

        if (novoNome == null || novoNome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da rotina não pode ser vazio.");
        }

        rotina.atualizar(novoNome, novaDescricao, novosHabitos);
        repositorioRotina.salvar(rotina);
    }

    public void deletarRotina(UUID rotinaId) {
        Rotina rotina = repositorioRotina.buscarPorId(rotinaId)
                .orElseThrow(() -> new IllegalArgumentException("Rotina com ID " + rotinaId + " não encontrada."));
        repositorioRotina.excluir(rotinaId);
    }

    public List<Rotina> listarRotinasDoUsuario(UUID usuarioId) {
        return repositorioRotina.listarTodosPorUsuario(usuarioId);
    }
}
