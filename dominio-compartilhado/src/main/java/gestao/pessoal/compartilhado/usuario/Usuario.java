package gestao.pessoal.compartilhado;

import java.util.UUID;

/**
 * Entidade de domínio que representa um usuário do sistema.
 * É a única fonte de verdade para as regras de negócio de um usuário.
 */
public class Usuario {

    // O ID é o único campo que permanece inalterável (final)
    private final UUID id;

    // Os campos nome, email e senha devem ser mutáveis para permitir a edição
    private String nome;
    private String email;
    private String senhaCriptografada;

    /**
     * Construtor para criar um novo usuário.
     * * @param nome Nome completo ou de usuário.
     * @param email Email do usuário.
     * @param senha Senha em texto limpo.
     */
    public Usuario(String nome, String email, String senha) {
        // Validações de criação inicial, conforme as regras do domínio
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido.");
        }
        if (senha == null || senha.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 6 caracteres.");
        }

        this.id = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        // Simulação de HASH para o exemplo. Em produção, use BCrypt.
        this.senhaCriptografada = criptografar(senha);
    }

    // --- MÉTODOS DE DOMÍNIO ---

    /**
     * Lógica de domínio para verificar se a senha fornecida corresponde à salva.
     * @param senhaFornecida Senha em texto limpo.
     * @return true se a senha for válida.
     */
    public boolean validarSenha(String senhaFornecida) {
        // Em um cenário real, usaria o des-hash do BCrypt, exemplo: passwordEncoder.matches(senhaFornecida, this.senhaCriptografada);
        String hashEsperado = criptografar(senhaFornecida);
        return this.senhaCriptografada.equals(hashEsperado);
    }

    /**
     * Simula a criptografia da senha para fins de teste.
     * @param senha Senha em texto limpo.
     * @return Versão "criptografada".
     */
    private String criptografar(String senha) {
        return "HASH_SEGURO_" + senha;
    }

    // --- GETTERS ---

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        throw new UnsupportedOperationException("A senha em texto puro não é acessível.");
    }


    public String getSenhaCriptografada() {
        return senhaCriptografada;
    }

    // --- SETTERS (MÉTODOS DE ALTERAÇÃO) ---
    // O Service é o único responsável por chamar estes métodos.

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        this.nome = nome;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido.");
        }
        this.email = email;
    }

    /**
     * Define uma nova senha, já criptografando-a.
     * @param novaSenha Senha em texto limpo.
     */
    public void setSenha(String novaSenha) {
        if (novaSenha == null || novaSenha.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 6 caracteres.");
        }
        // Aplica o hash antes de salvar
        this.senhaCriptografada = criptografar(novaSenha);
    }
}