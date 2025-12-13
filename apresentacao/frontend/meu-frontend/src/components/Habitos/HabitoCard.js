// HabitoCard.jsx

import React from 'react';
import '../../css/habitos.css';

const HabitoCard = ({
    habito,
    onRemover,
    onEditar,
    onVerDetalhes,
    fezCheckinHoje, // true/false determina o estado do botão
    onToggleCheckin,
    isCheckinLoading
}) => {

  const categoriaDisplay = habito.categoria || 'Geral';

  return (
    <div className="habito-card">
      <div className="habito-info">
        <div className="habito-title">{habito.nome}</div>
        <div className="habito-meta">
           <span style={{fontWeight: 600}}>{categoriaDisplay.toUpperCase()}</span>
           <span className="meta-separator"></span>
           <span>{habito.frequencia}</span>
        </div>
      </div>

      <div className="habito-actions">
        <button
            className="btn-outline"
            onClick={() => onVerDetalhes(habito)}
        >
            Ver detalhes
        </button>

        <button
            className="btn-outline"
            onClick={() => onEditar(habito)}
        >
            Editar
        </button>

        <button
          className="btn-outline"
          onClick={() => onToggleCheckin(habito.id, fezCheckinHoje)}
          // Desabilita se já estiver feito OU se estiver processando o check-in
          disabled={fezCheckinHoje || isCheckinLoading}
          style={{
            // Estilos para o estado CONCLUÍDO (congelado)
            backgroundColor: fezCheckinHoje ? '#2e7d32' : '#e8f5e9',
            borderColor: '#2e7d32',
            color: fezCheckinHoje ? '#FFFFFF' : '#2e7d32',
            fontWeight: 700,
            cursor: fezCheckinHoje || isCheckinLoading ? 'not-allowed' : 'pointer',
            opacity: fezCheckinHoje || isCheckinLoading ? 0.7 : 1
          }}
        >
           {isCheckinLoading
             ? 'Processando...'
             : fezCheckinHoje
               ? 'Hábito concluído' // Mensagem para concluído (botão congelado)
               : 'Marcar como feito'} // Mensagem para a fazer
        </button>

        <button className="btn-danger" onClick={() => onRemover(habito)}>
          Remover
        </button>
      </div>
    </div>
  );
};

export default HabitoCard;