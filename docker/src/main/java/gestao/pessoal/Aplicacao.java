package gestao.pessoal;

import gestao.pessoal.dominio.principal.princ.desafio.DesafioService;
import gestao.pessoal.dominio.principal.princ.desafio.RepositorioDesafio;
import gestao.pessoal.dominio.principal.compartilhado.usuario.RepositorioUsuario;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Aplicacao { // OU o nome da sua classe principal

    public static void main(String[] args) {
        SpringApplication.run(Aplicacao.class, args);
    }

    @Bean
    public DesafioService desafioService(
            RepositorioDesafio repositorioDesafio,
            RepositorioUsuario repositorioUsuario
    ) {
        return new DesafioService(repositorioDesafio, repositorioUsuario);
    }
}