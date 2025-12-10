package gestao.pessoal.aplicacao.compartilhado.usuario;

import gestao.pessoal.dominio.principal.compartilhado.usuario.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepositorioApl {

    void salvar(Usuario usuario);

    Optional<Usuario> buscarPorId(UUID id);

    Optional<UsuarioResumo> buscarResumoPorId(UUID id);

    Optional<UsuarioResumoExpandido> buscarResumoExpandidoPorId(UUID id);

    Optional<Usuario> buscarPorEmail(String email);

    void remover(UUID id);

}
