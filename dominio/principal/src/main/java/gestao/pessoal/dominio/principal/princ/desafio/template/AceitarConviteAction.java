package gestao.pessoal.dominio.principal.princ.desafio.template;

import gestao.pessoal.dominio.principal.princ.desafio.ConviteDesafio;
import gestao.pessoal.dominio.principal.princ.desafio.Desafio;
import gestao.pessoal.dominio.principal.princ.desafio.DesafioService;

import java.util.UUID;

public class AceitarConviteAction extends DesafioActionTemplate {

    private final UUID conviteId;

    public AceitarConviteAction(DesafioService repositorio, UUID conviteId) {
        super(repositorio);
        this.conviteId = conviteId;
    }

    @Override
    protected void validar(UUID usuarioId, Desafio desafio) {
        ConviteDesafio convite = repositorio.buscarConvitePorId(conviteId)
                .orElseThrow(() -> new IllegalArgumentException("Convite não encontrado"));

        if (!convite.getConvidadoId().equals(usuarioId))
            throw new IllegalArgumentException("Convite não pertence ao usuário");

        if (convite.getStatus() != ConviteDesafio.StatusConvite.PENDENTE)
            throw new IllegalStateException("Convite não está pendente");
    }

    @Override
    protected void acao(UUID usuarioId, Desafio desafio) {
        ConviteDesafio convite = repositorio.buscarConvitePorId(conviteId).get();
        convite.aceitar();
        repositorio.salvarConvite(convite);
        desafio = repositorio.buscarPorId(convite.getDesafioId()).get();
        desafio.adicionarParticipante(usuarioId);
        salvar(desafio);
    }
}
