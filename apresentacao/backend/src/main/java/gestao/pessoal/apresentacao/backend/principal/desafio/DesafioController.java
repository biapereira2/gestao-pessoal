package gestao.pessoal.apresentacao.backend.principal.desafio;

import gestao.pessoal.aplicacao.principal.desafio.DesafioServiceApl;
import gestao.pessoal.aplicacao.principal.desafio.DesafioResumo;
import gestao.pessoal.aplicacao.principal.desafio.ConviteResumo;
import gestao.pessoal.aplicacao.principal.desafio.ProgressoParticipanteDTO;
import gestao.pessoal.dominio.principal.princ.desafio.Desafio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/desafios")
public class DesafioController {

    private final DesafioServiceApl service;

    public DesafioController(DesafioServiceApl service) {
        this.service = service;
    }

    // --- 1. CRIAR DESAFIO E ENVIAR CONVITES ---
    @PostMapping
    public ResponseEntity<Desafio> criar(@RequestBody DesafioForm form) {
        try {
            Desafio novoDesafio = service.criarDesafio(
                    form.getCriadorId(),
                    form.getNome(),
                    form.getHabitosIds(),
                    form.getDataFim(),
                    form.getEmailsConvidados()
            );
            return ResponseEntity.ok(novoDesafio);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // --- 2. LISTAR DESAFIOS DO USUÁRIO ---
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<DesafioResumo>> listarMeusDesafios(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(service.listarDesafiosDoUsuario(usuarioId));
    }

    // --- 3. LISTAR CONVITES PENDENTES ---
    @GetMapping("/convites/pendentes/{usuarioId}")
    public ResponseEntity<List<ConviteResumo>> listarConvitesPendentes(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(service.listarConvitesPendentes(usuarioId));
    }

    // --- 4. ACEITAR CONVITE ---
    @PostMapping("/convites/{conviteId}/aceitar")
    public ResponseEntity<?> aceitarConvite(@PathVariable UUID conviteId, @RequestParam UUID usuarioId) {
        try {
            service.aceitarConvite(usuarioId, conviteId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- 5. REJEITAR CONVITE ---
    @PostMapping("/convites/{conviteId}/rejeitar")
    public ResponseEntity<?> rejeitarConvite(@PathVariable UUID conviteId, @RequestParam UUID usuarioId) {
        try {
            service.rejeitarConvite(usuarioId, conviteId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- 6. ACOMPANHAR PROGRESSO ---
    @GetMapping("/{desafioId}/progresso")
    public ResponseEntity<List<ProgressoParticipanteDTO>> acompanharProgresso(@PathVariable UUID desafioId) {
        return ResponseEntity.ok(service.acompanharProgresso(desafioId));
    }

    // --- 7. SAIR DO DESAFIO ---
    @DeleteMapping("/{desafioId}/sair")
    public ResponseEntity<Void> sairDoDesafio(@PathVariable UUID desafioId, @RequestParam UUID participanteId) {
        try {
            service.sairDoDesafio(participanteId, desafioId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // --- 8. ENCERRAR DESAFIO (Criador) ---
    @PostMapping("/{desafioId}/encerrar")
    public ResponseEntity<Void> encerrarDesafio(@PathVariable UUID desafioId, @RequestParam UUID criadorId) {
        try {
            service.encerrarDesafio(criadorId, desafioId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}