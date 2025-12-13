package gestao.pessoal.dominio.principal.princ.habito.decorator;

import java.time.LocalTime;

public class HabitoComRestricaoHorario extends HabitoDecorator {

    private final LocalTime inicio;
    private final LocalTime fim;

    public HabitoComRestricaoHorario(HabitoBase habito, LocalTime inicio, LocalTime fim) {
        super(habito);
        this.inicio = inicio;
        this.fim = fim;
    }

    public boolean podeRealizarAgora() {
        LocalTime agora = LocalTime.now();
        return !agora.isBefore(inicio) && !agora.isAfter(fim);
    }
}
