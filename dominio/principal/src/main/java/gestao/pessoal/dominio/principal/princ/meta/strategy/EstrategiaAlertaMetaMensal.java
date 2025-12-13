package gestao.pessoal.dominio.principal.princ.meta.strategy;

import gestao.pessoal.dominio.principal.princ.meta.Meta;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class EstrategiaAlertaMetaMensal implements EstrategiaAlertaMeta {

    @Override
    public boolean deveDispararAlerta(Meta meta) {
        double percentual = (double) meta.getHabitosCompletos() / meta.getQuantidade();
        long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), meta.getPrazo());

        return percentual < 0.25 || diasRestantes <= 2;
    }
}
