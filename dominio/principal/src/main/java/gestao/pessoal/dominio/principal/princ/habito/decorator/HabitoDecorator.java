package gestao.pessoal.dominio.principal.princ.habito.decorator;

public abstract class HabitoDecorator implements HabitoBase {

    protected HabitoBase habito;

    protected HabitoDecorator(HabitoBase habito) {
        this.habito = habito;
    }

    @Override
    public int getPontos() {
        return habito.getPontos();
    }

    @Override
    public String getNome() {
        return habito.getNome();
    }
}
