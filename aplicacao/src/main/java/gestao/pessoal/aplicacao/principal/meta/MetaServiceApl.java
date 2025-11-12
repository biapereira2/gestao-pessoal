package gestao.pessoal.aplicacao.principal.meta;


import gestao.pessoal.principal.meta.Meta;
import org.jmolecules.ddd.annotation.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MetaServiceApl {

    private final MetaRepositorioApl repositorio;

    public MetaServiceApl(MetaRepositorioApl repositorio) {
        this.repositorio = repositorio;
    }

    // Criar nova meta
    public void criar(Meta meta) {
        repositorio.salvar(meta);
    }

    // Buscar meta por ID
    public Optional<Meta> buscarPorId(UUID id) {
        return repositorio.buscarPorId(id);
    }

    // Listar resumos simples por usuário
    public List<MetaResumo> listarResumos(UUID usuarioId) {
        return repositorio.listarResumosPorUsuario(usuarioId);
    }

    // Listar resumos completos por usuário
    public List<MetaResumoExpandido> listarResumosExpandido(UUID usuarioId) {
        return repositorio.listarResumosExpandidoPorUsuario(usuarioId);
    }

    // Atualizar meta
    public void atualizar(Meta meta) {
        repositorio.salvar(meta);
    }

    // Remover meta
    public void remover(UUID id) {
        repositorio.remover(id);
    }
}
