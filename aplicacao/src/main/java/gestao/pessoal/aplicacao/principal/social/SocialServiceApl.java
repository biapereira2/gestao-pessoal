package gestao.pessoal.aplicacao.principal.social;

import gestao.pessoal.dominio.principal.compartilhado.usuario.RepositorioUsuario;
import gestao.pessoal.dominio.principal.compartilhado.usuario.Usuario;
import gestao.pessoal.dominio.principal.engajamento.amigo.AmigoDTO;
import gestao.pessoal.dominio.principal.engajamento.amigo.AmizadeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SocialServiceApl {

    private final AmizadeService amizadeService;
    private final RepositorioUsuario repoUsuario;

    public SocialServiceApl(AmizadeService amizadeService, RepositorioUsuario repoUsuario) {
        this.amizadeService = amizadeService;
        this.repoUsuario = repoUsuario;
    }

    public void adicionarAmigo(UUID usuarioId, UUID amigoId) {
        Usuario amigo = repoUsuario.buscarPorId(amigoId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário amigo não encontrado."));

        amizadeService.adicionarAmigo(usuarioId, amigo.getEmail());
    }

    public void removerAmigo(UUID usuarioId, UUID amigoId) {
        amizadeService.removerAmigo(usuarioId, amigoId);
    }

    public List<AmigoDTO> listarAmigos(UUID usuarioId) {
        return amizadeService.listarAmigos(usuarioId);
    }

    public List<AmigoDTO> pesquisarUsuarios(String termo) {
        List<Usuario> usuarios = repoUsuario.buscarPorParteDoNome(termo);

        return usuarios.stream()
                .map(u -> new AmigoDTO(u.getId(), u.getNome(), u.getEmail()))
                .collect(Collectors.toList());
    }
}