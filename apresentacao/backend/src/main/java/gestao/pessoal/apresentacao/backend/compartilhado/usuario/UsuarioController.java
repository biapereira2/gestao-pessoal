package gestao.pessoal.apresentacao.backend.compartilhado.usuario;

import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioServiceApl;
import gestao.pessoal.dominio.principal.compartilhado.usuario.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioServiceApl service;

    public UsuarioController(UsuarioServiceApl service) {
        this.service = service;
    }

    // --- CRIAR USUÁRIO ---
    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody UsuarioForm form) {
        try {
            Usuario usuario = new Usuario(form.getNome(), form.getEmail(), form.getSenha());
            service.criar(usuario);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // --- LOGIN ---
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody UsuarioForm form) {
        Optional<Usuario> usuarioOpt = service.buscarPorEmail(form.getEmail());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        Usuario usuario = usuarioOpt.get();
        if (!usuario.validarSenha(form.getSenha())) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(usuario);
    }

    // --- BUSCAR POR ID ---
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable UUID id) {
        Optional<Usuario> usuario = service.buscarPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --- ATUALIZAR USUÁRIO ---
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable UUID id, @RequestBody UsuarioForm form) {
        Optional<Usuario> usuarioOpt = service.buscarPorId(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Usuario usuario = usuarioOpt.get();
            usuario.setNome(form.getNome());
            usuario.setEmail(form.getEmail());
            usuario.setSenha(form.getSenha());

            service.atualizar(usuario);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // --- REMOVER USUÁRIO ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        Optional<Usuario> usuarioOpt = service.buscarPorId(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
