package gestao.pessoal.apresentacao.backend.principal.rotina;

import gestao.pessoal.aplicacao.principal.rotina.RotinaResumo;
import gestao.pessoal.aplicacao.principal.rotina.RotinaResumoExpandido;
import gestao.pessoal.aplicacao.principal.rotina.RotinaServiceApl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/rotinas")
public class RotinaController {

    private final RotinaServiceApl service;

    public RotinaController(RotinaServiceApl service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody RotinaForm form) {
        try {
            RotinaResumoExpandido rotina = new RotinaResumoExpandido(
                    UUID.randomUUID(),
                    form.getUsuarioId(),
                    form.getNome(),
                    form.getDescricao(),
                    form.getHabitosIds()
            );
            service.criar(rotina);
            return ResponseEntity.ok(rotina);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RotinaResumoExpandido> buscarPorId(@PathVariable UUID id) {
        Optional<RotinaResumoExpandido> rotina = service.buscarPorId(id);
        return rotina.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<RotinaResumo>> listarResumos(@PathVariable UUID usuarioId) {
        List<RotinaResumo> rotinas = service.listarResumos(usuarioId);
        return ResponseEntity.ok(rotinas);
    }

    @GetMapping("/usuario/{usuarioId}/expandido")
    public ResponseEntity<List<RotinaResumoExpandido>> listarResumosExpandido(@PathVariable UUID usuarioId) {
        List<RotinaResumoExpandido> rotinas = service.listarResumosExpandido(usuarioId);
        return ResponseEntity.ok(rotinas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RotinaResumoExpandido> atualizar(@PathVariable UUID id, @RequestBody RotinaForm form) {
        Optional<RotinaResumoExpandido> existente = service.buscarPorId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RotinaResumoExpandido rotinaAtual = existente.get();
        rotinaAtual.setNome(form.getNome());
        rotinaAtual.setDescricao(form.getDescricao());
        rotinaAtual.setHabitosIds(form.getHabitosIds());

        service.atualizar(rotinaAtual);
        return ResponseEntity.ok(rotinaAtual);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
