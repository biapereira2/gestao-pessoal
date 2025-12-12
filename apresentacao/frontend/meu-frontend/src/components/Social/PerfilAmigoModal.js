import React from 'react';

const PerfilAmigoModal = ({ amigo, onClose }) => {
  if (!amigo) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>

      <div className="modal-content" onClick={(e) => e.stopPropagation()}>

        {/* O CSS 'btn-close-modal' vai jogar este botão para a direita */}
        <button className="btn-close-modal" onClick={onClose}>✕</button>

        <div className="modal-header">
          <div className="modal-avatar-large">
            {amigo.nome ? amigo.nome.charAt(0).toUpperCase() : '?'}
          </div>
          <h2 className="modal-title">{amigo.nome}</h2>
          <span className="modal-badge">Amigo</span>
        </div>

        <div className="modal-body-simple">

          <div className="info-row">
            <label>Nome Completo</label>
            <div className="info-value">{amigo.nome}</div>
          </div>

          {/* --- ALTERADO AQUI --- */}
          {/* Saiu o ID, entrou o Email */}
          <div className="info-row">
            <label>E-mail de Contato</label>
            <div className="info-value">{amigo.email || "Email não disponível"}</div>
          </div>

        </div>

        <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: '20px' }}>
          <button className="btn-outline" onClick={onClose}>
            Fechar
          </button>
        </div>

      </div>
    </div>
  );
};

export default PerfilAmigoModal;