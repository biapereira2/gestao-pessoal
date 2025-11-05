package gestao.pessoal.usuario;

import java.util.Optional;
import java.util.UUID;

// Interface de Contrato para acesso a dados.
public interface RepositorioUsuario {
    void salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(UUID id);
    Optional<Usuario> buscarPorEmail(String email);
    boolean existePorEmail(String email);

}