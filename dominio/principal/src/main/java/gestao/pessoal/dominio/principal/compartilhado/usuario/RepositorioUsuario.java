package gestao.pessoal.dominio.principal.compartilhado.usuario;

import java.util.Optional;
import java.util.UUID;
import java.util.List;


public interface RepositorioUsuario {
    void salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(UUID id);
    Optional<Usuario> buscarPorEmail(String email);
    boolean existePorEmail(String email);
    List<Usuario> buscarPorParteDoNome(String nome);

}