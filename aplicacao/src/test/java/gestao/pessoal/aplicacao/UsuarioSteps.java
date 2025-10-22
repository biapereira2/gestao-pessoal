package gestao.pessoal.aplicacao;

import gestao.pessoal.compartilhado.Usuario;
import gestao.pessoal.compartilhado.RepositorioUsuario;
import gestao.pessoal.engajamento.PerfilSocial;
import gestao.pessoal.engajamento.RepositorioPerfilSocial;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioSteps {

    private UsuarioService service;
    private FakeRepositorioUsuario repositorio;
    private RepositorioPerfilSocial repositorioPerfil;
    private Map<String, String> dadosCadastro = new HashMap<>();
    private Exception excecaoCapturada;
    private String emailFinal;
    private Usuario usuarioLogado;
    private UUID usuarioIdParaEdicao;

    @Before
    public void setup() {
        this.repositorio = new FakeRepositorioUsuario();
        this.repositorioPerfil = new FakeRepositorioPerfilSocial();
        this.dadosCadastro.clear();
        this.excecaoCapturada = null;
        this.service = new UsuarioService(this.repositorio, this.repositorioPerfil);
    }

    @Dado("que eu estou na página de cadastro")
    public void queEuEstouNaPaginaDeCadastro() {
    }

    @Dado("que o email {string} já está cadastrado no sistema")
    public void queOEmailJaEstaCadastradoNoSistema(String email) {
        try {
            service.cadastrarNovoUsuario("Usuario Pre-cadastrado", email, "SenhaForte123");
        } catch (IllegalStateException e) {
        }
    }

    @Quando("eu preencho o campo {string} com {string}")
    public void euPreenchoOCampoCom(String campo, String valor) {
        // Armazena os dados do formulário para uso posterior
        dadosCadastro.put(campo, valor);
    }

    @Quando("eu clico no botão {string}")
    public void euClicoNoBotaoCadastrar(String botao) {
        if ("Cadastrar".equals(botao)) {
            String senha = dadosCadastro.get("Senha");
            String confirmaSenha = dadosCadastro.get("Confirmar Senha");
            this.emailFinal = dadosCadastro.get("Email");
            if (senha != null && confirmaSenha != null && !senha.equals(confirmaSenha)) {
                this.excecaoCapturada = new RuntimeException("As senhas não coincidem.");
                return;
            }

            try {
                service.cadastrarNovoUsuario(
                        dadosCadastro.get("Nome de Usuário"),
                        emailFinal,
                        senha
                );
            } catch (Exception e) {
                this.excecaoCapturada = e;
            }
        }
    }
    @Entao("o cadastro deve ser realizado com sucesso")
    public void oCadastroDeveSerRealizadoComSucesso() {
        assertNull(excecaoCapturada, "Ocorreu uma exceção inesperada: " + (excecaoCapturada != null ? excecaoCapturada.getMessage() : ""));
    }

    @Entao("o usuário {string} deve existir no sistema")
    public void oUsuarioDeveExistirNoSistema(String email) {
        assertTrue(repositorio.existePorEmail(email), "O usuário não foi salvo no repositório Mock.");
    }

    @Entao("o sistema deve retornar a mensagem de erro {string}")
    public void oSistemaDeveRetornarAMensagemDeErro(String mensagemEsperada) {
        assertNotNull(excecaoCapturada, "Deveria ter ocorrido um erro, mas o cadastro foi realizado com sucesso.");
        assertEquals(mensagemEsperada, excecaoCapturada.getMessage(), "A mensagem de erro não corresponde à esperada.");
    }

    @Dado("que o usuário {string} está logado")
    public void queOUsuarioEstaLogado(String nomeUsuario) {
        service.cadastrarNovoUsuario(nomeUsuario, "joao.silva@email.com", "Senha@123");
        this.usuarioLogado = service.login("joao.silva@email.com", "Senha@123");
        this.usuarioIdParaEdicao = usuarioLogado.getId();
    }

    @Quando("ele navega para a página de {string}")
    public void eleNavegaParaAPaginaDe(String pagina) {
        assertNotNull(usuarioLogado, "Usuário precisa estar logado antes de navegar.");
    }

    @Quando("ele preenche o campo {string} com {string}")
    public void elePreencheOCampoCom(String campo, String valor) {
        this.dadosCadastro.put(campo, valor);
    }

    @Quando("ele altera o campo {string} para {string}")
    public void eleAlteraOCampoPara(String campo, String valor) {
        dadosCadastro.put(campo, valor);
    }

    @Quando("ele fornece sua {string} corretamente")
    public void eleForneceSuaSenhaAtualCorretamente(String campo) {
        dadosCadastro.put("Senha Atual", "Senha@123");
    }

    @Quando("ele tenta alterar o campo {string} para {string}")
    public void eleTentaAlterarOCampoPara(String campo, String valor) {
        dadosCadastro.put(campo, valor);
    }

    @Quando("ele deixa o campo {string} vazio")
    public void eleDeixaOCampoVazio(String campo) {
        dadosCadastro.put(campo, "");
    }

    @Quando("ele clica no botão {string}")
    public void eleClicaNoBotao(String botao) {
        if ("Salvar Alterações".equals(botao)) {
            try {
                String nomeNovo = dadosCadastro.get("Nome de Usuário");
                String emailNovo = dadosCadastro.get("Email");
                String senhaAtual = dadosCadastro.get("Senha Atual");
                String novaSenha = dadosCadastro.get("Nova Senha");
                String confirmarNovaSenha = dadosCadastro.get("Confirmar Nova Senha");

                if (novaSenha != null && confirmarNovaSenha != null && !novaSenha.equals(confirmarNovaSenha)) {
                    throw new RuntimeException("As novas senhas não coincidem.");
                }

                service.editarPerfil(usuarioIdParaEdicao, nomeNovo, emailNovo, senhaAtual, novaSenha);

            } catch (Exception e) {
                this.excecaoCapturada = e;
            }
        } else if ("Cadastrar".equals(botao)) {
            String senha = dadosCadastro.get("Senha");
            String confirmaSenha = dadosCadastro.get("Confirmar Senha");
            this.emailFinal = dadosCadastro.get("Email");

            if (senha != null && confirmaSenha != null && !senha.equals(confirmaSenha)) {
                this.excecaoCapturada = new RuntimeException("As senhas não coincidem.");
                return;
            }

            try {
                service.cadastrarNovoUsuario(
                        dadosCadastro.get("Nome de Usuário"),
                        emailFinal,
                        senha
                );
            } catch (Exception e) {
                this.excecaoCapturada = e;
            }
        }
    }

    @Entao("ele deve ser redirecionado para a página de home")
    public void eleDeveSerRedirecionadoParaDashboard() {
        assertNull(excecaoCapturada, "Não deveria ocorrer erro durante edição: " + (excecaoCapturada != null ? excecaoCapturada.getMessage() : ""));
    }

    @Entao("ele deve ver a mensagem de sucesso {string}")
    public void eleDeveVerMensagemDeSucesso(String msg) {
        assertNull(excecaoCapturada, "Erro inesperado: " + (excecaoCapturada != null ? excecaoCapturada.getMessage() : ""));
    }

    @Entao("ele deve permanecer na página de {string}")
    public void eleDevePermanecerNaPagina(String pagina) {
        assertNotNull(excecaoCapturada, "Era esperado um erro, mas a edição foi bem-sucedida.");
    }

    @Entao("ele deve ver a mensagem de erro {string}")
    public void eleDeveVerMensagemDeErro(String mensagemEsperada) {
        assertNotNull(excecaoCapturada, "Era esperado um erro, mas não ocorreu.");

        String mensagemReal = excecaoCapturada.getMessage();
        assertTrue(mensagemReal.toLowerCase().contains(mensagemEsperada.toLowerCase()),
                "A mensagem de erro esperada ('" + mensagemEsperada + "') não foi encontrada ou não corresponde à mensagem real ('" + mensagemReal + "').");
    }

}