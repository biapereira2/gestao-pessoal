import React from 'react';
import '../../css/social.css';

const ConfirmacaoModal = ({ isOpen, onClose, onConfirm, titulo, mensagem }) => {
  if (!isOpen) return null;

  return (
    <div className="modal-overlay">
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>

        <div className="modal-header">
          <div style={{
            width: '60px', height: '60px', borderRadius: '50%',
            border: '2px solid #d32f2f', color: '#d32f2f',
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            fontSize: '30px', fontWeight: 'bold', marginBottom: '15px'
          }}>
            !
          </div>

          <h2 className="modal-title" style={{color: '#d32f2f'}}>{titulo}</h2>
          <p className="modal-subtitle" style={{marginTop: '10px'}}>{mensagem}</p>
        </div>

        <div style={{ display: 'flex', gap: '15px', marginTop: '30px' }}>
          <button
            className="btn-outline"
            style={{flex: 1, borderColor: '#ccc', color: '#666'}}
            onClick={onClose}
          >
            Cancelar
          </button>

          <button
            className="btn-danger"
            style={{flex: 1, backgroundColor: '#d32f2f', color: 'white'}}
            onClick={onConfirm}
          >
            Confirmar
          </button>
        </div>

      </div>
    </div>
  );
};

export default ConfirmacaoModal;