package gestao.pessoal.aplicacao;

import gestao.pessoal.compartilhado.Usuario;
import gestao.pessoal.compartilhado.RepositorioUsuario;

import java.util.UUID;

public class UsuarioService {
    // Depende da interface do repositório
    private final RepositorioUsuario repositorio;

    public UsuarioService(RepositorioUsuario repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Cadastra um novo usuário no sistema após validações.
     * @param nome O nome de usuário.
     * @param email O email do usuário (chave única).
     * @param senha A senha do usuário.
     */
    public void cadastrarNovoUsuario(String nome, String email, String senha) {
        if (repositorio.existePorEmail(email)) {
            throw new IllegalStateException("O email " + email + " já está cadastrado.");
        }

        // No futuro, teremos validações de VO aqui (Email, Senha)
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome de usuário inválido.");
        }
        if (senha == null || senha.length() < 6) { // Exemplo de regra simples
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        }

        // Simula o hash da senha (em uma implementação real, usariamos BCrypt)
        Usuario novoUsuario = new Usuario(nome, email, senha);
        repositorio.salvar(novoUsuario);
    }

    /**
     * Edita o perfil de um usuário existente.
     *
     * @param usuarioId ID do usuário logado.
     * @param nomeNovo Novo nome de usuário (pode ser null/vazio se não for alterado).
     * @param emailNovo Novo email (pode ser null/vazio se não for alterado).
     * @param senhaAtual Senha atual do usuário, obrigatória para alterar dados sensíveis.
     * @param novaSenha Nova senha (pode ser null/vazio se não for alterada).
     */
    public void editarPerfil(UUID usuarioId, String nomeNovo, String emailNovo, String senhaAtual, String novaSenha) {
        Usuario usuario = repositorio.buscarPorId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        // Se a senha atual não for fornecida e houver alteração de email ou senha, lance exceção.
        boolean alterandoDadosSensiveis = (emailNovo != null && !emailNovo.isEmpty() && !emailNovo.equals(usuario.getEmail()))
                || (novaSenha != null && !novaSenha.isEmpty());

        if (alterandoDadosSensiveis && (senhaAtual == null || senhaAtual.trim().isEmpty())) {
            throw new IllegalStateException("A senha atual é obrigatória para alterar dados sensíveis.");
        }

        // 1. Validar Senha Atual (Simulação)
        if (alterandoDadosSensiveis) {
            // Em um cenário real: if (!passwordEncoder.matches(senhaAtual, usuario.getSenhaHash()))
            if (!usuario.validarSenha(senhaAtual)) {
                throw new IllegalStateException("Senha atual incorreta.");
            }
        }

        // 2. Atualizar Nome
        if (nomeNovo != null && !nomeNovo.trim().isEmpty() && !nomeNovo.equals(usuario.getNome())) {
            usuario.setNome(nomeNovo);
        }

        // 3. Atualizar Email
        if (emailNovo != null && !emailNovo.trim().isEmpty() && !emailNovo.equals(usuario.getEmail())) {
            if (repositorio.existePorEmail(emailNovo)) {
                throw new IllegalStateException("O novo email já está cadastrado para outro usuário.");
            }
            usuario.setEmail(emailNovo);
        }

        // 4. Atualizar Senha
        if (novaSenha != null && !novaSenha.trim().isEmpty()) {
            if (novaSenha.length() < 6) {
                throw new IllegalArgumentException("A nova senha deve ter pelo menos 6 caracteres.");
            }
            // Em um cenário real, aqui seria o hash da nova senha
            usuario.setSenha(novaSenha);
        }

        // Salva as alterações no repositório
        repositorio.salvar(usuario);
    }

    // Método auxiliar para simular o login (necessário para os steps)
    public Usuario login(String email, String senha) {
        Usuario usuario = repositorio.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email ou senha inválidos."));

        // Em um cenário real: if (!passwordEncoder.matches(senha, usuario.getSenhaHash()))
        if (!usuario.validarSenha(senha)) {
            throw new IllegalArgumentException("Email ou senha inválidos.");
        }
        return usuario;
    }
}
