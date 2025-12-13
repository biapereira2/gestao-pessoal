package gestao.pessoal.apresentacao.backend.principal.habito;

import gestao.pessoal.aplicacao.principal.habito.*;
import gestao.pessoal.apresentacao.backend.principal.checkin.CheckInForm;
import gestao.pessoal.dominio.principal.princ.habito.Habito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/habitos")
@CrossOrigin(origins = "*")
public class HabitoController {

    private final HabitoServiceApl service;

    public HabitoController(HabitoServiceApl service) {
        this.service = service;
    }

    // =======================================================
    // CHECK-IN/DESMARQUE (NOVOS ENDPOINTS)
    // =======================================================

    /**
     * Marca o check-in de um hábito para uma data específica.
     * Endpoint: POST /habitos/{habitoId}/checkin
     */
    @PostMapping("/{habitoId}/checkin")
    public ResponseEntity<?> marcarCheckin(@PathVariable UUID habitoId, @RequestBody CheckInForm form) {
        try {
            // O service deve lidar com a lógica de persistir o check-in
            service.marcarCheckin(habitoId, form.getUsuarioId(), form.getData());
            return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created

        } catch (IllegalArgumentException e) {
            // Erro se o hábito não existir, etc.
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            // Erro de duplicidade, se o check-in já foi feito
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 409 Conflict
        }
    }

    /**
     * Desmarca (remove) o check-in de um hábito para uma data específica.
     * Endpoint: DELETE /habitos/{habitoId}/checkin
     */
    @DeleteMapping("/{habitoId}/checkin")
    // NOTE: Embora o frontend envie um body com DELETE, o Spring Boot
    // geralmente não espera @RequestBody em DELETE.
    // Para simplificar e seguir a prática REST, vamos passar o usuarioId e data via Query Params,
    // mas se o frontend não puder mudar, use @RequestBody (mas teste no Spring).
    public ResponseEntity<?> desmarcarCheckin(
            @PathVariable UUID habitoId,
            @RequestParam UUID usuarioId,
            @RequestParam String data) {

        try {
            // Converte a string da data para LocalDate
            java.time.LocalDate localDate = java.time.LocalDate.parse(data);

            // O service deve lidar com a lógica de remover o check-in
            service.desmarcarCheckin(habitoId, usuarioId, localDate);
            return ResponseEntity.noContent().build(); // 204 No Content

        } catch (java.time.format.DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Formato de data inválido. Use YYYY-MM-DD.");
        } catch (IllegalArgumentException e) {
            // Erro se o check-in não for encontrado para desmarcar
            return ResponseEntity.notFound().build();
        }
    }

    // =======================================================
    // CRUD EXISTENTE
    // =======================================================

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody HabitoForm form) {
        try {
            Habito habito = new Habito(
                    form.getUsuarioId(),
                    form.getNome(),
                    form.getDescricao(),
                    form.getCategoria(),
                    form.getFrequencia()
            );

            service.criar(habito);
            return ResponseEntity.ok(habito);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Habito> buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<HabitoResumo>> listarResumos(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(service.listarResumos(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/expandido")
    public ResponseEntity<List<HabitoResumoExpandido>> listarResumosExpandido(@PathVariable UUID usuarioId) {
        // NOTE: O service.listarResumosExpandido() deve ser ajustado para incluir
        // o status do check-in de hoje (fezCheckinHoje) em HabitoResumoExpandido.
        return ResponseEntity.ok(service.listarResumosExpandido(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/com-checkin")
    public ResponseEntity<List<HabitoResumoExpandido>> listarComCheckin(@PathVariable UUID usuarioId) {
        try {
            // O service deve retornar todos os hábitos e incluir fezCheckinHoje
            List<HabitoResumoExpandido> habitosComCheckin = service.listarResumosExpandido(usuarioId);
            return ResponseEntity.ok(habitosComCheckin);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable UUID id, @RequestBody HabitoForm form) {
        Optional<Habito> existente = service.buscarPorId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Habito habito = existente.get();
        habito.atualizar(
                form.getNome(),
                form.getDescricao(),
                form.getCategoria(),
                form.getFrequencia()
        );

        service.atualizar(habito);
        return ResponseEntity.ok(habito);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}