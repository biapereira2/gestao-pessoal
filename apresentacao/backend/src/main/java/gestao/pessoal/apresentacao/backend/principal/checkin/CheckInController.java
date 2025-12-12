package gestao.pessoal.apresentacao.backend.principal.checkin;

import gestao.pessoal.aplicacao.principal.checkin.CheckInResumo;
import gestao.pessoal.aplicacao.principal.checkin.CheckInServiceApl;
import gestao.pessoal.dominio.principal.princ.checkIn.CheckIn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/checkins")
public class CheckInController {

    private final CheckInServiceApl service;

    public CheckInController(CheckInServiceApl service) {
        this.service = service;
    }

    // Endpoint para Marcar Check-in (Criação de Recurso)
    @PostMapping
    public ResponseEntity<?> marcarCheckIn(@RequestBody CheckInForm form) {
        try {
            CheckIn checkIn = service.marcarCheckIn(
                    form.getUsuarioId(),
                    form.getHabitoId(),
                    form.getData()
            );
            return ResponseEntity.status(201).body(checkIn);
        } catch (IllegalStateException | IllegalArgumentException e) {
            // IllegalStateException: Duplicidade
            // IllegalArgumentException: Hábito não encontrado (vindo do Domínio)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint para Desmarcar Check-in (Exclusão de Recurso)
    // Usamos DELETE e passamos os dados no body/param pois não temos um ID conhecido externamente para o check-in
    @DeleteMapping
    public ResponseEntity<Void> desmarcarCheckIn(@RequestBody CheckInForm form) {
        try {
            service.desmarcarCheckIn(
                    form.getUsuarioId(),
                    form.getHabitoId(),
                    form.getData()
            );
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // Check-in não encontrado para desmarcar ou Hábito não encontrado
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para Listar Check-ins de um Hábito
    @GetMapping("/habito/{habitoId}/usuario/{usuarioId}")
    public ResponseEntity<List<CheckInResumo>> listarCheckInsPorHabito(
            @PathVariable UUID habitoId,
            @PathVariable UUID usuarioId) {

        List<CheckInResumo> checkIns = service.listarCheckInsPorHabito(usuarioId, habitoId);
        return ResponseEntity.ok(checkIns);
    }
}