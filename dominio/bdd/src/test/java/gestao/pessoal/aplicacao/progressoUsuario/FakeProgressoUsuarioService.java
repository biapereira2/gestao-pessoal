package gestao.pessoal.aplicacao.progressoUsuario;

import gestao.pessoal.engajamento.progressoUsuario.ProgressoUsuario;
import gestao.pessoal.engajamento.progressoUsuario.RepositorioProgressoUsuario;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// Classe ProgressoUsuario Simples para Teste
// ** Necessária, já que a classe real não foi fornecida **
class ProgressoUsuarioMock extends ProgressoUsuario {
    private final int nivel;

    // Assumindo que ProgressoUsuario tem um construtor que aceita UUID
    public ProgressoUsuarioMock(UUID usuarioId, int nivel) {
        // Super() é chamado implicitamente, assumindo que ProgressoUsuario é abstrata
        // ou tem um construtor acessível. Vamos simular.
        // Se a classe ProgressoUsuario for final/selada, isso pode falhar.
        super(usuarioId); // Assumindo construtor básico
        this.nivel = nivel;
    }

    // Sobrescrever o método get nivel
    @Override
    public int getNivel() {
        return nivel;
    }
}


// =================================================================
// IMPLEMENTAÇÃO MOCK (FAKE SERVICE PROGRESSO USUÁRIO - CORRIGIDO)
// =================================================================
public class FakeProgressoUsuarioService extends ProgressoUsuarioService {

    // Mapa para armazenar o nível mockado para cada usuário
    private final Map<UUID, Integer> niveisMockados = new HashMap<>();

    // Repositório Mínimo DUMMY para satisfazer o construtor da classe base
    private static final RepositorioProgressoUsuario DUMMY_REPO = new RepositorioProgressoUsuario() {
        @Override public void salvar(ProgressoUsuario progresso) {}
        @Override public Optional<ProgressoUsuario> buscarPorUsuarioId(UUID usuarioId) { return Optional.empty(); }
        @Override public boolean existeParaUsuario(UUID usuarioId) { return false; }
    };

    public FakeProgressoUsuarioService() {
        super(DUMMY_REPO);
    }

    @Override
    public void adicionarPontos(UUID usuarioId, int pontos, String motivo) {
        // Simulação: Apenas aceita a chamada sem lançar erro.
    }

    @Override
    public void removerPontos(UUID usuarioId, int pontos, String motivo) {
        // Simulação: Apenas aceita a chamada sem lançar erro.
    }

    // MÉTODO NOVO: Retorna o ProgressoUsuario mockado com o nível que foi setado
    @Override
    public ProgressoUsuario visualizarProgresso(UUID usuarioId) {
        // Retorna o nível mockado, ou 0 se não foi setado (nível inicial)
        int nivel = niveisMockados.getOrDefault(usuarioId, 0);
        // Cria uma instância mockada de ProgressoUsuario com o nível desejado
        return new ProgressoUsuarioMock(usuarioId, nivel);
    }

    // MÉTODO CORRIGIDO/ADICIONADO: Usado no gestao.pessoal.aplicacao.badges.BadgesSteps para setar o nível
    public void setNivelAtual(UUID usuarioId, int nivel) {
        // Salva o nível para ser retornado por visualizarProgresso
        niveisMockados.put(usuarioId, nivel);
    }
}