package gestao.pessoal.dominio.principal.compartilhado;

import gestao.pessoal.dominio.principal.engajamento.perfilSocial.PerfilSocial;
import gestao.pessoal.dominio.principal.engajamento.perfilSocial.RepositorioPerfilSocial;

import java.util.UUID;

public class UsuarioService {
    private final RepositorioUsuario repositorio;
    private final RepositorioPerfilSocial repositorioPerfil;

    public UsuarioService(RepositorioUsuario repositorio, RepositorioPerfilSocial repositorioPerfil) {
        this.repositorio = repositorio;
        this.repositorioPerfil = repositorioPerfil;
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

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome de usuário inválido.");
        }
        if (senha == null || senha.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        }

        Usuario novoUsuario = new Usuario(nome, email, senha);
        repositorio.salvar(novoUsuario);

        PerfilSocial novoPerfil = new PerfilSocial(novoUsuario.getId());
        repositorioPerfil.salvar(novoPerfil);

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

        boolean alterandoDadosSensiveis = (emailNovo != null && !emailNovo.isEmpty() && !emailNovo.equals(usuario.getEmail()))
                || (novaSenha != null && !novaSenha.isEmpty());

        if (alterandoDadosSensiveis && (senhaAtual == null || senhaAtual.trim().isEmpty())) {
            throw new IllegalStateException("A senha atual é obrigatória para alterar dados sensíveis.");
        }
        if (alterandoDadosSensiveis) {
            if (!usuario.validarSenha(senhaAtual)) {
                throw new IllegalStateException("Senha atual incorreta.");
            }
        }
        if (nomeNovo != null && !nomeNovo.trim().isEmpty() && !nomeNovo.equals(usuario.getNome())) {
            usuario.setNome(nomeNovo);
        }

        if (emailNovo != null && !emailNovo.trim().isEmpty() && !emailNovo.equals(usuario.getEmail())) {
            if (repositorio.existePorEmail(emailNovo)) {
                throw new IllegalStateException("O novo email já está cadastrado para outro usuário.");
            }
            usuario.setEmail(emailNovo);
        }

        if (novaSenha != null && !novaSenha.trim().isEmpty()) {
            if (novaSenha.length() < 6) {
                throw new IllegalArgumentException("A nova senha deve ter pelo menos 6 caracteres.");
            }
            usuario.setSenha(novaSenha);
        }
        repositorio.salvar(usuario);
    }

    public Usuario login(String email, String senha) {
        Usuario usuario = repositorio.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email ou senha inválidos."));

        if (!usuario.validarSenha(senha)) {
            throw new IllegalArgumentException("Email ou senha inválidos.");
        }
        return usuario;
    }
}
