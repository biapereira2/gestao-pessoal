package gestao.pessoal;

import static org.springframework.boot.SpringApplication.run;

import gestao.pessoal.aplicacao.meta.MetaService;
import gestao.pessoal.habito.habito.RepositorioHabito;
import gestao.pessoal.habito.meta.RepositorioMeta;
import gestao.pessoal.usuario.RepositorioUsuario;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Aplicacao {
    @Bean
    public MetaService metaService(RepositorioMeta repositorioMeta, RepositorioUsuario repositorioUsuario, RepositorioHabito repositorioHabito){
        return new MetaService(repositorioMeta,repositorioUsuario,repositorioHabito);
    }
}
