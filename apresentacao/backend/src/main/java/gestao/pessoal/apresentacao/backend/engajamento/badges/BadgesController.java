package gestao.pessoal.apresentacao.backend.engajamento.badges;

import gestao.pessoal.dominio.principal.engajamento.badges.Badges;
import gestao.pessoal.dominio.principal.engajamento.badges.BadgesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/badges")
@CrossOrigin(origins = "*")
public class BadgesController {

    private final BadgesService service;

    public BadgesController(BadgesService service) {
        this.service = service;
    }

    // 🔹 Lista badges conquistadas
    @GetMapping("/usuario/{usuarioId}/conquistadas")
    public ResponseEntity<List<Badges>> listarConquistadas(@PathVariable UUID usuarioId) {
        List<Badges> conquistas = service.listarConquistas(usuarioId);
        return ResponseEntity.ok(conquistas);
    }

    // 🔹 Lista badges disponíveis (a desbloquear)
    @GetMapping("/usuario/{usuarioId}/disponiveis")
    public ResponseEntity<List<Badges>> listarDisponiveis(@PathVariable UUID usuarioId) {
        List<Badges> disponiveis = service.listarBadgesDisponiveis(usuarioId);
        return ResponseEntity.ok(disponiveis);
    }

    // 🔹 Endpoint técnico para forçar verificação (útil em testes)
    @PostMapping("/usuario/{usuarioId}/verificar")
    public ResponseEntity<Void> verificarBadges(@PathVariable UUID usuarioId) {
        service.verificarEConcederBadges(usuarioId);
        return ResponseEntity.ok().build();
    }
}
