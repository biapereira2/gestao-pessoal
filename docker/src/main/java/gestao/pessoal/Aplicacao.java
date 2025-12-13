package gestao.pessoal;

import gestao.pessoal.dominio.principal.princ.desafio.DesafioService;
// Importa a implementação concreta que a camada de aplicação precisa
import gestao.pessoal.dominio.principal.princ.desafio.templateMethod.DesafioServicePorId;
import gestao.pessoal.dominio.principal.princ.desafio.RepositorioDesafio;
import gestao.pessoal.dominio.principal.compartilhado.usuario.RepositorioUsuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Aplicacao {

    public static void main(String[] args) {
        SpringApplication.run(Aplicacao.class, args);
    }

    @Bean
    public DesafioServicePorId desafioServicePorId(
            RepositorioDesafio repositorioDesafio,
            RepositorioUsuario repositorioUsuario
    ) {
        return new DesafioServicePorId(repositorioDesafio, repositorioUsuario);
    }
}