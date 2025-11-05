package gestao.pessoal.habito.meta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepositorioMeta {
    void salvar(Meta meta);
    Optional<Meta> buscarPorId(UUID id);
    List<Meta> listarPorUsuario(UUID usuarioId); // 🔗 Agora filtrando por usuário
    void remover(UUID id);
}
