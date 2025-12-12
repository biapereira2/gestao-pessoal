package gestao.pessoal.aplicacao.principal.checkin;

import gestao.pessoal.dominio.principal.princ.checkIn.CheckIn;
import gestao.pessoal.dominio.principal.princ.checkIn.CheckInService; // Serviço de Domínio
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CheckInServiceApl {

    private final CheckInService checkInServiceDominio; // Injeção do Serviço de Domínio
    private final CheckInRepositorioApl repositorio;

    // Construtor com injeção de dependências
    public CheckInServiceApl(CheckInService checkInServiceDominio, CheckInRepositorioApl repositorio) {
        this.checkInServiceDominio = checkInServiceDominio;
        this.repositorio = repositorio;
    }

    public CheckIn marcarCheckIn(UUID usuarioId, UUID habitoId, LocalDate data) {
        // Delega a lógica de negócio (incluindo a pontuação) para o Serviço de Domínio
        return checkInServiceDominio.marcarCheckIn(usuarioId, habitoId, data);
    }

    public void desmarcarCheckIn(UUID usuarioId, UUID habitoId, LocalDate data) {
        // Delega a lógica de negócio (incluindo a remoção da pontuação) para o Serviço de Domínio
        checkInServiceDominio.desmarcarCheckIn(usuarioId, habitoId, data);
    }

    // Método para listar os check-ins feitos em um hábito (Retorna um DTO simples de datas)
    public List<CheckInResumo> listarCheckInsPorHabito(UUID usuarioId, UUID habitoId) {
        return checkInServiceDominio.listarCheckInsPorHabito(usuarioId, habitoId)
                .stream()
                .map(ci -> new CheckInResumo(ci.getId(), ci.getHabitoId(), ci.getData()))
                .collect(Collectors.toList());
    }
}