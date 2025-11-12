package gestao.pessoal.aplicacao.checkIn;

import gestao.pessoal.principal.checkIn.CheckIn;
import gestao.pessoal.principal.checkIn.RepositorioCheckIn;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// =================================================================
// IMPLEMENTAÇÃO MOCK (FAKE REPOSITÓRIO CHECK-IN)
// =================================================================
public class FakeRepositorioCheckIn implements RepositorioCheckIn {

    private final Map<UUID, CheckIn> storage = new ConcurrentHashMap<>();

    @Override
    public void salvar(CheckIn checkIn) {
        storage.put(checkIn.getId(), checkIn);
    }

    @Override
    public void remover(UUID habitoId, LocalDate data, UUID usuarioId) {
        Optional<CheckIn> checkInParaRemover = storage.values().stream()
                .filter(c -> c.getHabitoId().equals(habitoId))
                .filter(c -> c.getData().equals(data))
                .filter(c -> c.getUsuarioId().equals(usuarioId))
                .findFirst();

        checkInParaRemover.ifPresent(checkIn -> storage.remove(checkIn.getId()));
    }

    @Override
    public Optional<CheckIn> buscarPorHabitoEData(UUID habitoId, LocalDate data, UUID usuarioId) {
        return storage.values().stream()
                .filter(c -> c.getHabitoId().equals(habitoId))
                .filter(c -> c.getData().equals(data))
                .filter(c -> c.getUsuarioId().equals(usuarioId))
                .findFirst();
    }

    @Override
    public List<CheckIn> listarPorHabito(UUID habitoId, UUID usuarioId) {
        return storage.values().stream()
                .filter(c -> c.getHabitoId().equals(habitoId))
                .filter(c -> c.getUsuarioId().equals(usuarioId))
                .collect(Collectors.toList());
    }

    public void limpar() {
        storage.clear();
    }
}
