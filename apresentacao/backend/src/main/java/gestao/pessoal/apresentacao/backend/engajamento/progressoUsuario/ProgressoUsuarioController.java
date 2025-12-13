package gestao.pessoal.apresentacao.backend.engajamento.progressoUsuario;

import gestao.pessoal.dominio.principal.engajamento.progressoUsuario.ProgressoUsuario;
import gestao.pessoal.dominio.principal.engajamento.progressoUsuario.ProgressoUsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/progresso")
@CrossOrigin(origins = "*")
public class ProgressoUsuarioController {

    private final ProgressoUsuarioService service;

    public ProgressoUsuarioController(ProgressoUsuarioService service) {
        this.service = service;
    }

    // 🔹 Cenário 1 – Adicionar pontos
    @PostMapping("/{usuarioId}/adicionar-pontos")
    public ResponseEntity<?> adicionarPontos(
            @PathVariable UUID usuarioId,
            @RequestBody PontuacaoForm form
    ) {
        try {
            service.adicionarPontos(usuarioId, form.getPontos(), form.getMotivo());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 🔹 Cenário 2 – Remover pontos
    @PostMapping("/{usuarioId}/remover-pontos")
    public ResponseEntity<?> removerPontos(
            @PathVariable UUID usuarioId,
            @RequestBody PontuacaoForm form
    ) {
        try {
            service.removerPontos(usuarioId, form.getPontos(), form.getMotivo());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 🔹 Cenário 4 – Visualizar progresso
    @GetMapping("/{usuarioId}")
    public ResponseEntity<ProgressoUsuario> visualizar(@PathVariable UUID usuarioId) {
        try {
            ProgressoUsuario progresso = service.visualizarProgresso(usuarioId);
            return ResponseEntity.ok(progresso);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

