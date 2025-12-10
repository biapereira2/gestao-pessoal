package gestao.pessoal.infra.persistencia.jpa.compartilhado.usuario;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "USUARIO")
public class UsuarioJpa {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senhaCriptografada;

    // --- GETTERS & SETTERS ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaCriptografada() { return senhaCriptografada; }
    public void setSenhaCriptografada(String senhaCriptografada) {
        this.senhaCriptografada = senhaCriptografada;
    }
}
