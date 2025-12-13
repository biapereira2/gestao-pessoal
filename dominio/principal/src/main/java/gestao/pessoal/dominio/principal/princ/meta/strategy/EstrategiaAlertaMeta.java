package gestao.pessoal.dominio.principal.princ.meta.strategy;

import gestao.pessoal.dominio.principal.princ.meta.Meta;

public interface EstrategiaAlertaMeta {
    boolean deveDispararAlerta(Meta meta);
}
