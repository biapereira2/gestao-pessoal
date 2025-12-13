package gestao.pessoal.apresentacao.backend.engajamento.badges;

import gestao.pessoal.dominio.principal.engajamento.badges.Badges;

import java.util.List;

public class BadgesResponseDTO {

    private List<Badges> conquistadas;
    private List<Badges> disponiveis;

    public BadgesResponseDTO(List<Badges> conquistadas, List<Badges> disponiveis) {
        this.conquistadas = conquistadas;
        this.disponiveis = disponiveis;
    }

    public List<Badges> getConquistadas() {
        return conquistadas;
    }

    public List<Badges> getDisponiveis() {
        return disponiveis;
    }
}
