package gestao.pessoal.jpa.aplicacao.checkIn;

import gestao.pessoal.jpa.aplicacao.habito.FakeRepositorioHabito;
import gestao.pessoal.dominio.principal.engajamento.progressoUsuario.ProgressoUsuarioService;
import gestao.pessoal.dominio.principal.princ.checkIn.CheckInService;

/**
 * Implementação FAKE do CheckInService que usa os Repositórios FAKE
 * e o ProgressoUsuarioService.
 */
public class FakeCheckInService extends CheckInService {
    public FakeCheckInService(
            FakeRepositorioCheckIn repositorioCheckIn,
            FakeRepositorioHabito repositorioHabito,
            ProgressoUsuarioService progressoUsuarioService) {

        super(repositorioCheckIn, repositorioHabito, progressoUsuarioService);
    }
}
