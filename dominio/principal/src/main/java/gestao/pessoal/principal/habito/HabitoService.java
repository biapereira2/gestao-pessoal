package gestao.pessoal.principal.habito;


import java.util.List;
import java.util.UUID;

public class HabitoService {

    private final RepositorioHabito repositorioHabito;

    public HabitoService(RepositorioHabito repositorioHabito) {
        this.repositorioHabito = repositorioHabito;
    }

    public Habito criar(UUID usuarioId, String nome, String descricao, String categoria, String frequencia) {
        boolean jaExiste = this.repositorioHabito.listarTodosPorUsuario(usuarioId).stream()
                .anyMatch(habitoExistente -> habitoExistente.getNome().equalsIgnoreCase(nome.trim()));

        if (jaExiste) {
            throw new IllegalStateException("O hábito já existe");
        }
        Habito novoHabito = new Habito(usuarioId, nome, descricao, categoria, frequencia);
        repositorioHabito.salvar(novoHabito);
        return novoHabito;
    }

    public void atualizar(UUID habitoId, String novoNome, String novaDescricao, String novaCategoria, String novaFrequencia) {
        Habito habito = repositorioHabito.buscarPorId(habitoId)
                .orElseThrow(() -> new IllegalArgumentException("Hábito com ID " + habitoId + " não encontrado."));

        habito.atualizar(novoNome, novaDescricao, novaCategoria, novaFrequencia);
        repositorioHabito.salvar(habito);
    }

    public List<Habito> listarPorUsuario(UUID usuarioId) {
        return repositorioHabito.listarTodosPorUsuario(usuarioId);
    }

    public void excluir(UUID habitoId) {
        repositorioHabito.buscarPorId(habitoId)
                .orElseThrow(() -> new IllegalArgumentException("Hábito com ID " + habitoId + " não encontrado."));
        repositorioHabito.excluir(habitoId);
    }
}