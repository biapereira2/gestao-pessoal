package gestao.pessoal.aplicacao.principal.social;

import gestao.pessoal.dominio.principal.compartilhado.usuario.RepositorioUsuario;
import gestao.pessoal.dominio.principal.compartilhado.usuario.Usuario;
import gestao.pessoal.dominio.principal.engajamento.amigo.AmigoDTO;
import gestao.pessoal.dominio.principal.engajamento.amigo.AmizadeService;
import gestao.pessoal.dominio.principal.engajamento.perfilSocial.RepositorioPerfilSocial;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SocialServiceApl {

    private final AmizadeService amizadeService;
    private final RepositorioUsuario repoUsuario;

    public SocialServiceApl(RepositorioPerfilSocial repoSocial, RepositorioUsuario repoUsuario) {
        this.repoUsuario = repoUsuario;
        this.amizadeService = new AmizadeService(repoSocial, repoUsuario);
    }

    public void adicionarAmigo(UUID usuarioId, UUID amigoId) {
        // TRADUÇÃO: A API recebe o ID, mas o seu Domínio pede o Email.
        // Buscamos o usuário no banco para pegar o email.
        Usuario amigo = repoUsuario.buscarPorId(amigoId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário amigo não encontrado."));

        // Chama a regra de negócio do domínio passando o email
        amizadeService.adicionarAmigo(usuarioId, amigo.getEmail());
    }

    public void removerAmigo(UUID usuarioId, UUID amigoId) {
        amizadeService.removerAmigo(usuarioId, amigoId);
    }

    public List<AmigoDTO> listarAmigos(UUID usuarioId) {
        return amizadeService.listarAmigos(usuarioId);
    }
}