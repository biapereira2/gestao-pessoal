package gestao.pessoal.dominio.principal.princ.checkIn;

import gestao.pessoal.dominio.principal.engajamento.progressoUsuario.ProgressoUsuarioService;
import gestao.pessoal.dominio.principal.princ.habito.RepositorioHabito; // Adicionado para buscar os pontos do Hábito
import gestao.pessoal.dominio.principal.princ.habito.Habito; // Adicionado para buscar os pontos do Hábito

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CheckInService {

    private final RepositorioCheckIn repositorioCheckIn;
    private final RepositorioHabito repositorioHabito;
    private final ProgressoUsuarioService progressoUsuarioService; // Serviço do Domínio-Engajamento

    // Construtor atualizado para injeção das dependências de outros contextos/domínios
    public CheckInService(
            RepositorioCheckIn repositorioCheckIn,
            RepositorioHabito repositorioHabito,
            ProgressoUsuarioService progressoUsuarioService) {
        this.repositorioCheckIn = repositorioCheckIn;
        this.repositorioHabito = repositorioHabito;
        this.progressoUsuarioService = progressoUsuarioService;
    }


    public CheckIn marcarCheckIn(UUID usuarioId, UUID habitoId, LocalDate data) {
        // 1. Regra de Negócio: Impedir check-in duplicado na mesma data
        boolean jaExiste = repositorioCheckIn.buscarPorHabitoEData(habitoId, data, usuarioId).isPresent();

        if (jaExiste) {
            throw new IllegalStateException("Check-in para este hábito já foi registrado na data " + data);
        }

        // 2. Criação e Persistência do Fato (Check-in)
        CheckIn checkIn = new CheckIn(habitoId, usuarioId, data);
        repositorioCheckIn.salvar(checkIn);

        // 3. Orquestração (Integração com o Domínio-Engajamento)

        // Busca o Hábito para saber quantos pontos ele vale
        Habito habito = repositorioHabito.buscarPorId(habitoId)
                .orElseThrow(() -> new IllegalArgumentException("Hábito com ID " + habitoId + " não encontrado."));

        // Adiciona os pontos ao Progresso do Usuário (Domínio-Engajamento)
        progressoUsuarioService.adicionarPontos(
                usuarioId,
                habito.getPontos(), // Valor base do Hábito
                "Check-in de " + habito.getNome() + " em " + data
        );

        // TODO: Lógica futura para Atualizar Sequência e Metas deve ser chamada aqui.

        return checkIn;
    }


    public void desmarcarCheckIn(UUID usuarioId, UUID habitoId, LocalDate data) {
        // 1. Validação
        repositorioCheckIn.buscarPorHabitoEData(habitoId, data, usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Nenhum check-in encontrado para essa data."));

        // 2. Orquestração (Reversão de Integração com o Domínio-Engajamento)

        // Busca o Hábito para saber quantos pontos devem ser removidos
        Habito habito = repositorioHabito.buscarPorId(habitoId)
                .orElseThrow(() -> new IllegalArgumentException("Hábito com ID " + habitoId + " não encontrado."));

        // Remove os pontos do Progresso do Usuário (Domínio-Engajamento)
        progressoUsuarioService.removerPontos(
                usuarioId,
                habito.getPontos(),
                "Desmarcação de check-in de " + habito.getNome() + " em " + data
        );

        // TODO: Lógica futura para Reverter Sequência e Metas deve ser chamada aqui.

        // 3. Remoção do Fato (Check-in)
        repositorioCheckIn.remover(habitoId, data, usuarioId);
    }


    public List<CheckIn> listarCheckInsPorHabito(UUID usuarioId, UUID habitoId) {
        return repositorioCheckIn.listarPorHabito(habitoId, usuarioId);
    }
}