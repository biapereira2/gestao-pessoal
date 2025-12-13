package gestao.pessoal.dominio.principal.princ.habito.decorator;

public class HabitoComPontuacaoExtra extends HabitoDecorator {

    private final int bonus;

    public HabitoComPontuacaoExtra(HabitoBase habito, int bonus) {
        super(habito);
        this.bonus = bonus;
    }

    @Override
    public int getPontos() {
        return super.getPontos() + bonus;
    }
}
