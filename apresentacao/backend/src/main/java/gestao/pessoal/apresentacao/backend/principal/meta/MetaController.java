package gestao.pessoal.apresentacao.backend.principal.meta;

import gestao.pessoal.aplicacao.principal.meta.MetaServiceApl;
import gestao.pessoal.aplicacao.principal.meta.MetaResumo;
import gestao.pessoal.aplicacao.principal.meta.MetaResumoExpandido;
import gestao.pessoal.dominio.principal.princ.meta.Meta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/metas")
@CrossOrigin(origins = "*")
public class MetaController {

    private final MetaServiceApl service;

    public MetaController(MetaServiceApl service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody MetaForm form) {
        try {
            Meta meta = new Meta(
                    form.getUsuarioId(),
                    form.getTipo(),
                    form.getDescricao(),
                    form.getHabitosIds().size(),
                    form.getHabitosIds()
            );

            service.criar(meta);

            // 🔹 Atualiza habitosCompletos imediatamente
            for (UUID habitoId : meta.getHabitosIds()) {
                service.atualizarMetasAssociadas(habitoId, meta.getUsuarioId(), LocalDate.now());
            }

            return ResponseEntity.ok(meta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meta> buscarPorId(@PathVariable UUID id) {
        Optional<Meta> meta = service.buscarPorId(id);
        return meta.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MetaResumo>> listarResumos(@PathVariable UUID usuarioId) {
        List<MetaResumo> metas = service.listarResumos(usuarioId);
        return ResponseEntity.ok(metas);
    }

    @GetMapping("/usuario/{usuarioId}/expandido")
    public ResponseEntity<List<MetaResumoExpandido>> listarResumosExpandido(@PathVariable UUID usuarioId) {
        List<MetaResumoExpandido> metas = service.listarResumosExpandido(usuarioId);
        return ResponseEntity.ok(metas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Meta> atualizar(@PathVariable UUID id, @RequestBody MetaForm form) {
        Optional<Meta> existente = service.buscarPorId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Meta metaAtual = existente.get();
        metaAtual.setQuantidade(form.getQuantidade() != null ? form.getQuantidade() : metaAtual.getQuantidade());
        metaAtual.setPrazo(form.getPrazo() != null ? form.getPrazo() : metaAtual.getPrazo());
        metaAtual.setAlertaProximoFalha(form.isAlertaProximoFalha());

        if (form.getHabitosIds() != null && !form.getHabitosIds().isEmpty()) {
            metaAtual.setHabitosIds(form.getHabitosIds());
            metaAtual.setQuantidade(form.getHabitosIds().size());

            for (UUID habitoId : metaAtual.getHabitosIds()) {
                service.atualizarMetasAssociadas(habitoId, metaAtual.getUsuarioId(), LocalDate.now());
            }
        }

        service.atualizar(metaAtual);
        return ResponseEntity.ok(metaAtual);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
