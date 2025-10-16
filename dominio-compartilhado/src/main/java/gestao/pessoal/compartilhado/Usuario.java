package gestao.pessoal.compartilhado;

import java.util.UUID;

public class Usuario {
    private final UUID id;
    private final String nome;
    private final String email;
    private final String senhaCriptografada;

    public Usuario(String nome, String email, String senha) {
        // Regras de Validação Pura (Domínio)
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
        // Simulação de HASH para o exemplo
        this.senhaCriptografada = "HASH_SEGURO_" + senha;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    // Lógica Pura: Validar se a senha fornecida bate com a salva
    public boolean validarSenha(String senhaFornecida) {
        String hashEsperado = "HASH_SEGURO_" + senhaFornecida;
        return this.senhaCriptografada.equals(hashEsperado);
    }
}