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

    // 🔹 Endpoint único que retorna badges conquistadas e disponíveis
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> listarTodas(@PathVariable String usuarioId) {
        try {
            UUID id = UUID.fromString(usuarioId);
            System.out.println("Buscando badges para usuário: " + id);

            List<Badges> conquistadas = service.listarConquistas(id);
            List<Badges> disponiveis = service.listarBadgesDisponiveis(id);

            System.out.println("Conquistadas: " + conquistadas.size());
            System.out.println("Disponiveis: " + disponiveis.size());

            BadgesResponseDTO response = new BadgesResponseDTO(conquistadas, disponiveis);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return ResponseEntity.status(500)
                    .body("Erro inesperado ao listar badges do usuário.");
        }
    }


    // 🔹 Endpoint técnico para forçar verificação
    @PostMapping("/usuario/{usuarioId}/verificar")
    public ResponseEntity<?> verificarBadges(@PathVariable String usuarioId) {
        try {
            UUID id = UUID.fromString(usuarioId);
            service.verificarEConcederBadges(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Erro inesperado ao verificar badges.");
        }
    }
}
