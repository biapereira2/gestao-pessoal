package gestao.pessoal.apresentacao.backend.compartilhado.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioForm {

    @NotNull
    @Size(min = 1, max = 100)
    private String nome;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 6, max = 100)
    private String senha;

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
