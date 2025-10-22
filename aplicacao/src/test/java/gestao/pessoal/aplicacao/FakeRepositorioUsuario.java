package gestao.pessoal.aplicacao;

import gestao.pessoal.compartilhado.RepositorioUsuario;
import gestao.pessoal.compartilhado.Usuario;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FakeRepositorioUsuario implements RepositorioUsuario {
    private final Map<String, Usuario> usuarios = new HashMap<>();

    @Override
    public void salvar(Usuario usuario) {
        usuarios.put(usuario.getEmail(), usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarios.values().stream()
                .filter(usuario -> usuario.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return Optional.ofNullable(usuarios.get(email));
    }

    @Override
    public boolean existePorEmail(String email) {
        return usuarios.containsKey(email);
    }

    public Map<String, Usuario> getUsuarios() { return usuarios; }

    public void limpar() {
        usuarios.clear();
    }
}
