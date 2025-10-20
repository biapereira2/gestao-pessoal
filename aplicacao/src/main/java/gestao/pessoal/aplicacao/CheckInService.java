package gestao.pessoal.aplicacao;

import gestao.pessoal.habito.CheckIn;
import gestao.pessoal.habito.RepositorioCheckIn;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CheckInService {

    private final RepositorioCheckIn repositorioCheckIn;

    public CheckInService(RepositorioCheckIn repositorioCheckIn) {
        this.repositorioCheckIn = repositorioCheckIn;
    }


    public CheckIn marcarCheckIn(UUID usuarioId, UUID habitoId, LocalDate data) {
        boolean jaExiste = repositorioCheckIn.buscarPorHabitoEData(habitoId, data, usuarioId).isPresent();

        if (jaExiste) {
            throw new IllegalStateException("check-in para este hábito já foi registrado");
        }

        CheckIn checkIn = new CheckIn(habitoId, usuarioId, data);
        repositorioCheckIn.salvar(checkIn);
        return checkIn;
    }



    public void desmarcarCheckIn(UUID usuarioId, UUID habitoId, LocalDate data) {
        CheckIn checkIn = repositorioCheckIn.buscarPorHabitoEData(habitoId, data, usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Nenhum check-in encontrado para essa data."));
        repositorioCheckIn.remover(habitoId, data, usuarioId);
    }


    public List<CheckIn> listarCheckInsPorHabito(UUID usuarioId, UUID habitoId) {
        return repositorioCheckIn.listarPorHabito(habitoId, usuarioId);
    }
}
