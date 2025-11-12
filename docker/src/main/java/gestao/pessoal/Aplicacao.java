package gestao.pessoal;

import static org.springframework.boot.SpringApplication.run;

import gestao.pessoal.compartilhado.usuario.RepositorioUsuario;
import gestao.pessoal.principal.meta.MetaService;
import gestao.pessoal.principal.habito.RepositorioHabito;
import gestao.pessoal.principal.meta.RepositorioMeta;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Aplicacao {
    @Bean
    public MetaService metaService(RepositorioMeta repositorioMeta, RepositorioUsuario repositorioUsuario, RepositorioHabito repositorioHabito){
        return new MetaService(repositorioMeta,repositorioUsuario,repositorioHabito);
    }
}
