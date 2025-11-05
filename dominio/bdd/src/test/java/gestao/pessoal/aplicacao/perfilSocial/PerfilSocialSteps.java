package gestao.pessoal.aplicacao.perfilSocial;

import gestao.pessoal.aplicacao.amigo.AmigoDTO;
import gestao.pessoal.aplicacao.amigo.AmizadeService;
import gestao.pessoal.aplicacao.usuario.FakeRepositorioUsuario;
import gestao.pessoal.aplicacao.usuario.UsuarioService;
import gestao.pessoal.usuario.RepositorioUsuario;
import gestao.pessoal.usuario.Usuario;
import gestao.pessoal.engajamento.perfilSocial.RepositorioPerfilSocial;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class PerfilSocialSteps {
    private AmizadeService amizadeService;
    private UsuarioService usuarioService;
    private RepositorioPerfilSocial repositorioPerfil;
    private RepositorioUsuario repositorioUsuario;
    private Usuario usuarioLogado;
    private List<AmigoDTO> listaDeAmigosResultante;
    private Exception excecaoLancada;

    public PerfilSocialSteps() {
        this.repositorioPerfil = new FakeRepositorioPerfilSocial();
        this.repositorioUsuario = new FakeRepositorioUsuario();
        this.usuarioService = new UsuarioService(repositorioUsuario, repositorioPerfil);
        this.amizadeService = new AmizadeService(repositorioPerfil, repositorioUsuario);
        this.excecaoLancada = null;
        this.listaDeAmigosResultante = new ArrayList<>();
    }

    @Dado("que o usuário {string} com email {string} está cadastrado")
    public void que_o_usuario_com_email_esta_cadastrado(String nome, String email) {
        try {
            this.usuarioService.cadastrarNovoUsuario(nome, email, "senha123");
        } catch (Exception e) {
        }
    }

    @Dado("que os usuários {string} com email {string} e {string} com email {string} estão cadastrados")
    public void que_os_usuarios_com_email_e_com_email_estao_cadastrados(String nome1, String email1, String nome2, String email2) {
        que_o_usuario_com_email_esta_cadastrado(nome1, email1);
        que_o_usuario_com_email_esta_cadastrado(nome2, email2);
    }

    @Dado("que os usuários {string} com email {string}, {string} com email {string} e {string} com email {string} estão cadastrados")
    public void que_os_usuarios_com_email_e_com_email_estao_cadastrados(String nome1, String email1, String nome2, String email2, String nome3, String email3) {
        que_o_usuario_com_email_esta_cadastrado(nome1, email1);
        que_o_usuario_com_email_esta_cadastrado(nome2, email2);
        que_o_usuario_com_email_esta_cadastrado(nome3, email3);
    }

    @Dado("eu estou logado como {string}")
    public void que_estou_logado_como(String email) {
        this.usuarioLogado = this.repositorioUsuario.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Falha no setup: Usuário " + email + " não encontrado."));
    }

    @Dado("que os usuários {string} com email {string} e {string} com email {string} são amigos")
    public void que_os_usuarios_com_email_e_com_email_sao_amigos(String nome1, String email1, String nome2, String email2) {
        que_os_usuarios_com_email_e_com_email_estao_cadastrados(nome1, email1, nome2, email2);
        que_estou_logado_como(email1);
        eu_adiciono_o_usuario_com_email_como_amigo(email2);
    }

    @Quando("eu adiciono o usuário com email {string} como amigo")
    public void eu_adiciono_o_usuario_com_email_como_amigo(String emailAmigo) {
        try {
            this.amizadeService.adicionarAmigo(this.usuarioLogado.getId(), emailAmigo);
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @Quando("eu tento adicionar o usuário com email {string} como amigo")
    public void eu_tento_adicionar_o_usuario_com_email_como_amigo(String emailAmigo) {
        try {
            this.amizadeService.adicionarAmigo(this.usuarioLogado.getId(), emailAmigo);
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @Entao("o usuário {string} deve estar na minha lista de amigos")
    public void o_usuario_deve_estar_na_minha_lista_de_amigos(String nomeAmigo) {
        List<AmigoDTO> amigos = this.amizadeService.listarAmigos(this.usuarioLogado.getId());
        boolean encontrado = amigos.stream().anyMatch(a -> a.nome().equals(nomeAmigo));
        assertTrue(encontrado, "O amigo '" + nomeAmigo + "' não foi encontrado na lista.");
    }

    @E("eu devo estar na lista de amigos de {string}")
    public void eu_devo_estar_na_lista_de_amigos_de(String nomeAmigo) {
        Usuario amigo = this.repositorioUsuario.buscarPorEmail(nomeAmigo.toLowerCase() + "@email.com")
                .orElseThrow(() -> new IllegalStateException("Amigo '" + nomeAmigo + "' não encontrado para verificação."));

        List<AmigoDTO> listaDoAmigo = this.amizadeService.listarAmigos(amigo.getId());

        boolean encontrado = listaDoAmigo.stream().anyMatch(a -> a.id().equals(this.usuarioLogado.getId()));
        assertTrue(encontrado, "Eu ('" + usuarioLogado.getNome() + "') não fui encontrado na lista de '" + nomeAmigo + "'.");
    }

    @Quando("eu removo o usuário com email {string} dos meus amigos")
    public void eu_removo_o_usuario_com_email_dos_meus_amigos(String emailAmigo) {
        try {
            Usuario amigoParaRemover = this.repositorioUsuario.buscarPorEmail(emailAmigo).get();
            this.amizadeService.removerAmigo(this.usuarioLogado.getId(), amigoParaRemover.getId());
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @Entao("o usuário {string} não deve estar na minha lista de amigos")
    public void o_usuario_nao_deve_estar_na_minha_lista_de_amigos(String nomeAmigo) {
        List<AmigoDTO> amigos = this.amizadeService.listarAmigos(this.usuarioLogado.getId());
        boolean encontrado = amigos.stream().anyMatch(a -> a.nome().equals(nomeAmigo));
        assertFalse(encontrado, "O amigo '" + nomeAmigo + "' ainda está na lista, mas não deveria.");
    }

    @E("eu não devo estar na lista de amigos de {string}")
    public void eu_nao_devo_estar_na_lista_de_amigos_de(String nomeAmigo) {
        Usuario amigo = this.repositorioUsuario.buscarPorEmail(nomeAmigo.toLowerCase() + "@email.com").get();
        List<AmigoDTO> listaDoAmigo = this.amizadeService.listarAmigos(amigo.getId());

        boolean encontrado = listaDoAmigo.stream().anyMatch(a -> a.id().equals(this.usuarioLogado.getId()));
        assertFalse(encontrado, "Eu ('" + usuarioLogado.getNome() + "') ainda estou na lista de '" + nomeAmigo + "'.");
    }

    @E("eu adicionei {string} e {string} como amigos")
    public void eu_adicionei_e_como_amigos(String emailAmigo1, String emailAmigo2) {
        eu_adiciono_o_usuario_com_email_como_amigo(emailAmigo1);
        eu_adiciono_o_usuario_com_email_como_amigo(emailAmigo2);
    }

    @Quando("eu acesso minha lista de amigos")
    public void eu_acesso_minha_lista_de_amigos() {
        this.listaDeAmigosResultante = this.amizadeService.listarAmigos(this.usuarioLogado.getId());
    }

    @Entao("minha lista de amigos deve conter {int} usuários")
    public void minha_lista_de_amigos_deve_conter_usuarios(Integer quantidade) {
        assertEquals(quantidade, this.listaDeAmigosResultante.size());
    }

    @E("a lista deve incluir os nomes {string} e {string}")
    public void a_lista_deve_incluir_os_nomes_e(String nome1, String nome2) {
        List<String> nomesNaLista = this.listaDeAmigosResultante.stream().map(AmigoDTO::nome).toList();
        assertTrue(nomesNaLista.containsAll(List.of(nome1, nome2)), "A lista não contém ambos os nomes esperados.");
    }

    @Entao("minha lista de amigos deve estar vazia")
    public void minha_lista_de_amigos_deve_estar_vazia() {
        assertEquals(0, this.listaDeAmigosResultante.size());
    }

    @Entao("eu devo receber um erro de amizade informando {string}")
    public void eu_devo_receber_um_erro_de_amizade_informando(String mensagemDeErro) {
        assertNotNull(excecaoLancada, "Nenhuma exceção foi lançada, mas era esperada.");
        assertTrue(excecaoLancada.getMessage().toLowerCase().contains(mensagemDeErro.toLowerCase()),
                "A mensagem de erro esperada era '" + mensagemDeErro + "', mas foi '" + excecaoLancada.getMessage() + "'");
    }
}
