import React from 'react';
import '../../css/alertas.css';
import { differenceInDays } from 'date-fns';

const AlertaCard = ({ alerta, onVerDetalhes, onEditar, onRemover }) => {
  if (!alerta) return null;

  // Cria a data de forma segura
  const dataValida = alerta.dataDisparo ? new Date(alerta.dataDisparo) : null;
  const agora = new Date();

  const isDataValida = dataValida && !isNaN(dataValida);

  const isDisparado =
    alerta.disparado || (isDataValida && dataValida < agora);

  const statusCor = isDisparado ? '#D32F2F' : '#2E7D32';
  const statusTexto = isDisparado ? 'Disparado' : 'Ativo';

  // Texto de dias restantes
  let diasTexto = 'Data não definida';

  if (isDataValida) {
    const dias = differenceInDays(dataValida, agora);

    if (dias > 1) {
      diasTexto = `${dias} dias restantes`;
    } else if (dias === 1) {
      diasTexto = '1 dia restante';
    } else if (dias === 0) {
      diasTexto = 'Hoje';
    } else {
      diasTexto = `${Math.abs(dias)} dias atrás`;
    }
  }

  return (
    <div
      className="meta-card"
      style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}
    >
      <div className="meta-info">
        <div className="meta-title">{alerta.titulo}</div>

        <div className="meta-meta">
          Categoria: {alerta.categoria || 'Geral'}
          <span className="meta-separator"></span>

          <span style={{ fontWeight: 500 }}>
            {diasTexto}
          </span>

          <span className="meta-separator"></span>

          <span style={{ color: statusCor, fontWeight: 600 }}>
            {statusTexto}
          </span>
        </div>
      </div>

      <div className="meta-actions">
        <button
          className="btn-outline"
          onClick={() => onVerDetalhes(alerta)}
        >
          Ver
        </button>

        <button
          className="btn-outline"
          onClick={() => onEditar(alerta)}
        >
          Editar
        </button>

        <button
          className="btn-danger"
          onClick={() => onRemover(alerta)}
        >
          Apagar
        </button>
      </div>
    </div>
  );
};

export default AlertaCard;