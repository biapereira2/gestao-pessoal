package gestao.pessoal.aplicacao.principal.checkin;

import gestao.pessoal.dominio.principal.princ.checkIn.CheckIn;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CheckInRepositorioApl {

    void salvar(CheckIn checkIn);
    void remover(UUID habitoId, LocalDate data, UUID usuarioId);
    Optional<CheckIn> buscarPorHabitoEData(UUID habitoId, LocalDate data, UUID usuarioId);
    List<CheckIn> listarPorHabito(UUID habitoId, UUID usuarioId);
    List<LocalDate> listarDatasPorHabito(UUID habitoId, UUID usuarioId); // DTO simplificado
}