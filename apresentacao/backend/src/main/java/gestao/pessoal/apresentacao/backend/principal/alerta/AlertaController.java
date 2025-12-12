package gestao.pessoal.apresentacao.backend.principal.alerta;

import gestao.pessoal.aplicacao.principal.alerta.AlertaServiceApl;
import gestao.pessoal.dominio.principal.princ.alerta.Alerta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/alertas")
@CrossOrigin(origins = "*")
public class AlertaController {

    private final AlertaServiceApl service;

    public AlertaController(AlertaServiceApl service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody AlertaForm form) {
        try {
            Alerta alerta = new Alerta(
                    form.getUsuarioId(),
                    form.getTitulo(),
                    form.getDescricao(),
                    form.getDataDisparo(),
                    form.getCategoria()
            );
            service.criar(alerta);
            return ResponseEntity.ok(alerta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<?>> listarPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(service.listarPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/expandido")
    public ResponseEntity<List<?>> listarResumosExpandido(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(service.listarResumosExpandidoPorUsuario(usuarioId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable UUID id, @RequestBody AlertaForm form) {
        try {
            Alerta alertaAtualizado = service.editar(
                    id,
                    form.getTitulo(),
                    form.getDescricao(),
                    form.getDataDisparo(),
                    form.getCategoria()
            );
            return ResponseEntity.ok(alertaAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
