package gestao.pessoal.habito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepositorioMeta {
    void salvar(Meta meta);
    Optional<Meta> buscarPorId(UUID metaId);
    List<Meta> listarTodasPorUsuario(UUID usuarioId);
    void excluir(UUID metaId);
}
