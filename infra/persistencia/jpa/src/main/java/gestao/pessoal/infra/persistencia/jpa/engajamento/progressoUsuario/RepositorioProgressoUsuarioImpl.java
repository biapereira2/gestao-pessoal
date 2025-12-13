package gestao.pessoal.infra.persistencia.jpa.engajamento.progressoUsuario;

import gestao.pessoal.aplicacao.engajamento.progressoUsuario.ProgressoUsuarioRepositorioApl;
import gestao.pessoal.aplicacao.engajamento.progressoUsuario.ProgressoUsuarioResumo;
import gestao.pessoal.dominio.principal.engajamento.progressoUsuario.ProgressoUsuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class RepositorioProgressoUsuarioImpl
        implements ProgressoUsuarioRepositorioApl {

    private final ProgressoUsuarioJpaRepositorio jpaRepositorio;

    public RepositorioProgressoUsuarioImpl(
            ProgressoUsuarioJpaRepositorio jpaRepositorio
    ) {
        this.jpaRepositorio = jpaRepositorio;
    }

    // =========================
    // Escrita / domínio
    // =========================
    @Override
    public void salvar(ProgressoUsuario progresso) {
        ProgressoUsuarioJpa jpa = mapearParaJpa(progresso);
        jpaRepositorio.save(jpa);
    }

    // =========================
    // Leitura rica (domínio)
    // =========================
    @Override
    public Optional<ProgressoUsuario> buscarPorUsuarioId(UUID usuarioId) {
        return jpaRepositorio.findById(usuarioId)
                .map(this::mapearParaDominio);
    }

    // =========================
    // Leitura leve (resumo)
    // =========================
    @Override
    public Optional<ProgressoUsuarioResumo> buscarResumoPorUsuarioId(UUID usuarioId) {
        return jpaRepositorio.findById(usuarioId)
                .map(jpa -> new ProgressoUsuarioResumo(
                        jpa.getUsuarioId(),
                        jpa.getPontos(),
                        jpa.getNivel(),
                        jpa.getLimiteNivelAtual(),
                        jpa.getLimiteProximoNivel(),
                        jpa.getLimiteProximoNivel() - jpa.getPontos()
                ));
    }

    /* =========================
       MAPEAMENTO MANUAL
       ========================= */

    private ProgressoUsuario mapearParaDominio(ProgressoUsuarioJpa jpa) {
        ProgressoUsuario progresso =
                new ProgressoUsuario(jpa.getUsuarioId());

        progresso.definirEstadoPersistido(
                jpa.getPontos(),
                jpa.getNivel(),
                jpa.getLimiteNivelAtual(),
                jpa.getLimiteProximoNivel()
        );

        return progresso;
    }

    private ProgressoUsuarioJpa mapearParaJpa(ProgressoUsuario progresso) {
        ProgressoUsuarioJpa jpa = new ProgressoUsuarioJpa();

        jpa.setUsuarioId(progresso.getUsuarioId());
        jpa.setPontos(progresso.getPontos());
        jpa.setNivel(progresso.getNivel());
        jpa.setLimiteNivelAtual(progresso.getLimiteNivelAtual());
        jpa.setLimiteProximoNivel(progresso.getLimiteProximoNivel());

        return jpa;
    }
}
