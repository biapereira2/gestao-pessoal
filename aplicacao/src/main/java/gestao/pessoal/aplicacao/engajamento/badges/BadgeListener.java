package gestao.pessoal.aplicacao.engajamento.badges;

import gestao.pessoal.dominio.principal.engajamento.amigo.evento.AmizadeCriadaEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BadgeListener {

    private final BadgesServiceApl badgesServiceApl;

    public BadgeListener(BadgesServiceApl badgesServiceApl) {
        this.badgesServiceApl = badgesServiceApl;
    }
    @EventListener
    public void quandoAmizadeForCriada(AmizadeCriadaEvent event) {
        System.out.println("Verificando badges para o usuário: " + event.getUsuarioId());
        badgesServiceApl.verificarEConceder(event.getUsuarioId());
        System.out.println("Verificando badges para o novo amigo: " + event.getNovoAmigoId());
        badgesServiceApl.verificarEConceder(event.getNovoAmigoId());
    }
}