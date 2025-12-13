package gestao.pessoal.apresentacao.backend.engajamento.progressoUsuario;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class PontuacaoForm {

    @Min(1)
    private int pontos;

    @NotBlank
    private String motivo;

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
