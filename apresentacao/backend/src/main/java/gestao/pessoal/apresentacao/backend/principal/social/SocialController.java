package gestao.pessoal.apresentacao.backend.principal.social;

import gestao.pessoal.aplicacao.principal.social.SocialServiceApl;
import gestao.pessoal.dominio.principal.engajamento.amigo.AmigoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/social")
@CrossOrigin(origins = "*")
public class SocialController {

    private final SocialServiceApl serviceApl;

    public SocialController(SocialServiceApl serviceApl) {
        this.serviceApl = serviceApl;
    }

    @GetMapping("/{usuarioId}/amigos")
    public List<AmigoDTO> listarAmigos(@PathVariable UUID usuarioId) {
        return serviceApl.listarAmigos(usuarioId);
    }

    @PostMapping("/{usuarioId}/amigos")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adicionarAmigo(
            @PathVariable UUID usuarioId,
            @RequestBody SocialForm form) {

        serviceApl.adicionarAmigo(usuarioId, form.getAmigoId());
    }

    @DeleteMapping("/{usuarioId}/amigos/{amigoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerAmigo(@PathVariable UUID usuarioId, @PathVariable UUID amigoId) {
        serviceApl.removerAmigo(usuarioId, amigoId);
    }

    @GetMapping("/pesquisar")
    public List<AmigoDTO> pesquisarUsuarios(@RequestParam String termo) {
        return serviceApl.pesquisarUsuarios(termo);
    }
}