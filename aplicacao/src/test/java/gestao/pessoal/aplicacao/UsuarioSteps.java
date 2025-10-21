package gestao.pessoal.aplicacao;

import gestao.pessoal.compartilhado.Usuario;
import gestao.pessoal.compartilhado.RepositorioUsuario;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

// =================================================================
// IMPLEMENTAÇÃO MOCK (FAKE REPOSITÓRIO)
// Este mock simula o banco de dados no módulo de teste.
// =================================================================
class FakeRepositorioUsuario implements RepositorioUsuario {
    private final Map<String, Usuario> usuarios = new HashMap<>();

    // O serviço de aplicação (UsuarioService) só usa a interface RepositorioUsuario
    @Override
    public void salvar(Usuario usuario) {
        usuarios.put(usuario.getEmail(), usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return Optional.empty();
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


// =================================================================
// STEPS DEFINITION (MAPEAMENTO DO GHERKIN)
// =================================================================
public class UsuarioSteps {

    private UsuarioService service;
    private FakeRepositorioUsuario repositorio;
    private Map<String, String> dadosCadastro = new HashMap<>();
    private Exception excecaoCapturada;
    private String emailFinal;

    // Reseta o estado antes de cada cenário (limpa o mock de banco de dados)
    @Before
    public void setup() {
        this.repositorio = new FakeRepositorioUsuario();
        // O Service aceita a interface, e passamos a implementação Mock
        this.service = new UsuarioService(this.repositorio);
        this.dadosCadastro.clear();
        this.excecaoCapturada = null;
    }

    // --- STEPS 'DADO' ---
    @Dado("que eu estou na página de cadastro")
    public void queEuEstouNaPaginaDeCadastro() {
        // Inicialização do ambiente de teste.
    }

    @Dado("que o email {string} já está cadastrado no sistema")
    public void queOEmailJaEstaCadastradoNoSistema(String email) {
        try {
            // Usa o Service para garantir que a Entidade seja criada corretamente
            service.cadastrarNovoUsuario("Usuario Pre-cadastrado", email, "SenhaForte123");
        } catch (IllegalStateException e) {
            // Ignora se o mock já tiver algo, garantindo a pré-condição
        }
    }

    // --- STEPS 'QUANDO' ---
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

            // Simula a checagem de Front-end/Apresentação para Senhas não coincidentes
            if (senha != null && confirmaSenha != null && !senha.equals(confirmaSenha)) {
                this.excecaoCapturada = new RuntimeException("As senhas não coincidem.");
                return;
            }

            try {
                // Chama a lógica de negócio (Service)
                service.cadastrarNovoUsuario(
                        dadosCadastro.get("Nome de Usuário"),
                        emailFinal,
                        senha
                );
            } catch (Exception e) {
                // Captura qualquer exceção lançada pelo Service
                this.excecaoCapturada = e;
            }
        }
    }

    // --- STEPS 'ENTÃO' ---
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
        // Compara a mensagem da exceção capturada
        assertEquals(mensagemEsperada, excecaoCapturada.getMessage(), "A mensagem de erro não corresponde à esperada.");
    }
}