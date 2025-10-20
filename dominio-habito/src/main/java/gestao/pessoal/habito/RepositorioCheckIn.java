package gestao.pessoal.habito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepositorioCheckIn {
    void salvar(CheckIn checkIn);
    void remover(UUID habitoId, LocalDate data, UUID usuarioId);
    Optional<CheckIn> buscarPorHabitoEData(UUID habitoId, LocalDate data, UUID usuarioId);
    List<CheckIn> listarPorHabito(UUID habitoId, UUID usuarioId);
}
