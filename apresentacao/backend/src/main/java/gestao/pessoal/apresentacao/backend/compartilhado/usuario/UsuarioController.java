package gestao.pessoal.apresentacao.backend.compartilhado.usuario;

import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioServiceApl;
import gestao.pessoal.dominio.principal.compartilhado.usuario.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioServiceApl service;

    public UsuarioController(UsuarioServiceApl service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody UsuarioForm form) {
        Usuario usuario = new Usuario(
                form.getNome(),
                form.getEmail(),
                form.getSenha()
        );

        service.criar(usuario);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable UUID id) {
        Optional<Usuario> usuario = service.buscarPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable UUID id, @RequestBody UsuarioForm form) {
        Optional<Usuario> existente = service.buscarPorId(id);

        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = existente.get();
        usuario.setNome(form.getNome());
        usuario.setEmail(form.getEmail());
        usuario.setSenha(form.getSenha());

        service.atualizar(usuario);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
