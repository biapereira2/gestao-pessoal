package gestao.pessoal.aplicacao;

import gestao.pessoal.habito.Habito;
import gestao.pessoal.habito.RepositorioHabito;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class HabitoRepositorioEmMemoria implements RepositorioHabito {

    private final Map<UUID, Habito> habitos = new HashMap<>();

    @Override
    public void salvar(Habito habito) {
        habitos.put(habito.getId(), habito);
    }
    @Override
    public Optional<Habito> buscarPorId(UUID habitoId) {
        // .ofNullable() cria um Optional vazio se o resultado de .get() for null.
        return Optional.ofNullable(habitos.get(habitoId));
    }

    @Override
    public List<Habito> listarTodosPorUsuario(UUID usuarioId) {
        return habitos.values()
                .stream()
                .filter(habito -> habito.getUsuarioId().equals(usuarioId))
                .collect(Collectors.toList());
    }

    @Override
    public void excluir(UUID habitoId) {
        habitos.remove(habitoId);
    }
}