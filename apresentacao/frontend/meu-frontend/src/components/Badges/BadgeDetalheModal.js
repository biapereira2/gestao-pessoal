// src/components/Progresso/BadgeDetalheModal.js
import React from 'react';
import './badges.css';

const BadgeDetalheModal = ({ badge, onClose }) => {
  if (!badge) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div
        className="modal-content-creation badge-modal"
        onClick={e => e.stopPropagation()}
      >
        <button className="btn-close-absolute" onClick={onClose}>✕</button>

        <div className="badge-modal-header">
          <div className="badge-icon-large">{badge.icone || '🏅'}</div>
          <h2>{badge.nome}</h2>
          <span className="badge-tier">{badge.nivel}</span>
        </div>

        <p className="badge-descricao">
          {badge.descricao}
        </p>

        {badge.dataConquista && (
          <p className="badge-data">
            Conquistado em {new Date(badge.dataConquista).toLocaleDateString()}
          </p>
        )}

        <button className="btn-outline btn-block" onClick={onClose}>
          Fechar
        </button>
      </div>
    </div>
  );
};

export default BadgeDetalheModal;
