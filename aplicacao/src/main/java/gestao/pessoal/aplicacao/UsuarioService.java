package gestao.pessoal.aplicacao;

import gestao.pessoal.compartilhado.RepositorioUsuario;
import gestao.pessoal.compartilhado.Usuario;
import java.util.UUID;

// Esta classe orquestra as regras para as histórias de Cadastro e Login
public class UsuarioService {

    // CORRETO: Depende apenas da Interface (Contrato)
    private final RepositorioUsuario repositorioUsuario;

    public UsuarioService(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    // História de Usuário: Cadastrar um novo Usuário
    public Usuario cadastrarNovoUsuario(String nome, String email, String senha) {

        if (repositorioUsuario.existePorEmail(email)) {
            throw new IllegalStateException("Já existe uma conta cadastrada com este email.");
        }
        // Cria a Entidade usando o construtor simples que gera o ID
        Usuario novoUsuario = new Usuario(nome, email, senha);

        repositorioUsuario.salvar(novoUsuario);
        return novoUsuario;
    }

    // História de Usuário: Login do Usuário
    public Usuario login(String email, String senha) {

        // 1. Busca o usuário
        Usuario usuario = repositorioUsuario.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas."));

        // 2. Valida a senha (Domínio)
        if (!usuario.validarSenha(senha)) {
            // Lançamos a mesma exceção para evitar dicas a invasores
            throw new IllegalArgumentException("Credenciais inválidas.");
        }

        return usuario;
    }
}