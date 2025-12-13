// src/components/Progresso/BarraProgressoNivel.js
import React from 'react';
import './progresso.css';

const BarraProgressoNivel = ({ atual, total }) => {
  const progresso = total > 0
    ? Math.round((atual / total) * 100)
    : 0;

  return (
    <div className="nivel-barra-container">
      <div className="nivel-barra-info">
        <span>{atual} / {total} XP</span>
        <span>{progresso}%</span>
      </div>

      <div className="nivel-barra">
        <div
          className="nivel-barra-preenchida"
          style={{ width: `${progresso}%` }}
        />
      </div>
    </div>
  );
};

export default BarraProgressoNivel;
