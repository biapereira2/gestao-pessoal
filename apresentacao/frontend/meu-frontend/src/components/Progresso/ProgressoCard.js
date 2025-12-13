// src/components/Progresso/ProgressoCard.js
import React from 'react';
import BarraProgressoNivel from './BarraProgressoNivel';
import './progresso.css';

const ProgressoCard = ({ nivel, xpAtual, xpProximoNivel }) => {
  return (
    <div className="progresso-card">
      <div className="progresso-header">
        <h2>Nível {nivel}</h2>
        <span className="progresso-subtitle">
          Continue evoluindo 🚀
        </span>
      </div>

      <BarraProgressoNivel
        atual={xpAtual}
        total={xpProximoNivel}
      />
    </div>
  );
};

export default ProgressoCard;
