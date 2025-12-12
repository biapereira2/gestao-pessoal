package gestao.pessoal.apresentacao.backend.principal.habito;

import gestao.pessoal.aplicacao.principal.habito.*;
import gestao.pessoal.dominio.principal.princ.habito.Habito;
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
        return ResponseEntity.ok(service.listarResumosExpandido(usuarioId));
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
