package gestao.pessoal;

// Importa a interface/classe abstrata DesafioService
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

    /**
     * Define o Bean para a implementação concreta DesafioServicePorId.
     * Esta classe concreta (DesafioServicePorId) estende o Template Method (DesafioService)
     * e implementa a lógica de busca por ID, sendo a escolha ideal para a camada de aplicação que lida com UUIDs.
     * * O Spring agora pode injetar este Bean (@Bean public DesafioServicePorId ...) no DesafioServiceAplImpl.
     */
    @Bean
    public DesafioServicePorId desafioServicePorId(
            RepositorioDesafio repositorioDesafio,
            RepositorioUsuario repositorioUsuario
    ) {
        // INSTANCIANDO A CLASSE CONCRETA
        return new DesafioServicePorId(repositorioDesafio, repositorioUsuario);
    }
}