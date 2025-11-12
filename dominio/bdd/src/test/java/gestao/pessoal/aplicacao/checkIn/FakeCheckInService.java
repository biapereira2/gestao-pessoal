package gestao.pessoal.aplicacao.checkIn;

import gestao.pessoal.aplicacao.habito.FakeRepositorioHabito;
import gestao.pessoal.engajamento.progressoUsuario.ProgressoUsuarioService;
import gestao.pessoal.principal.checkIn.CheckInService;

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
