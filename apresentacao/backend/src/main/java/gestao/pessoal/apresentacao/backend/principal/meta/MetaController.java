package gestao.pessoal.apresentacao.backend.principal.meta;

import gestao.pessoal.aplicacao.principal.meta.MetaServiceApl;
import gestao.pessoal.aplicacao.principal.meta.MetaResumo;
import gestao.pessoal.aplicacao.principal.meta.MetaResumoExpandido;
import gestao.pessoal.dominio.principal.princ.meta.Meta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/metas")
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
                    UUID.randomUUID(),
                    form.getTipo(),
                    form.getDescricao(),
                    form.getQuantidade()
            );
            service.criar(meta);
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
        metaAtual.setQuantidade(form.getQuantidade());
        metaAtual.setHabitosCompletos(form.getHabitosCompletos());
        metaAtual.setPrazo(form.getPrazo());
        metaAtual.setAlertaProximoFalha(form.isAlertaProximoFalha());

        service.atualizar(metaAtual);
        return ResponseEntity.ok(metaAtual);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
