package gestao.pessoal.aplicacao;

import gestao.pessoal.compartilhado.RepositorioUsuario;
import gestao.pessoal.compartilhado.Usuario;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UsuarioRepositorioEmMemoria implements RepositorioUsuario {

    private final Map<UUID, Usuario> usuarios = new HashMap<>();

    // Um "índice" para permitir buscas rápidas por email, evitando percorrer todos os usuários
    private final Map<String, UUID> indiceDeEmail = new HashMap<>();

    @Override
    public void salvar(Usuario usuario) {
        // Valida se o email já existe para um usuário DIFERENTE antes de salvar
        if (existePorEmail(usuario.getEmail())) {
            // Permite salvar se o email pertence ao mesmo usuário (caso de atualização de dados)
            if (!buscarPorEmail(usuario.getEmail()).get().getId().equals(usuario.getId())) {
                throw new IllegalStateException("O email fornecido já está em uso por outra conta.");
            }
        }

        usuarios.put(usuario.getId(), usuario);
        indiceDeEmail.put(usuario.getEmail(), usuario.getId());
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        UUID usuarioId = indiceDeEmail.get(email);
        if (usuarioId == null) {
            return Optional.empty();
        }
        return buscarPorId(usuarioId);
    }

    @Override
    public boolean existePorEmail(String email) {
        return indiceDeEmail.containsKey(email);
    }
}