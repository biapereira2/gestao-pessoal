package gestao.pessoal.dominio.principal.princ.alerta.observer;

import gestao.pessoal.dominio.principal.princ.alerta.Alerta;

public interface AlertaObserver {
    void alterado(Alerta alerta);
}
