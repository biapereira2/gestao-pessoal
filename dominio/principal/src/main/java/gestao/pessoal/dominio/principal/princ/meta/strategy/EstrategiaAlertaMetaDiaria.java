package gestao.pessoal.dominio.principal.princ.meta.strategy;

import gestao.pessoal.dominio.principal.princ.meta.Meta;

public class EstrategiaAlertaMetaDiaria implements EstrategiaAlertaMeta {

    @Override
    public boolean deveDispararAlerta(Meta meta) {
        // Metas diárias não entram em alerta de falha
        return false;
    }
}
