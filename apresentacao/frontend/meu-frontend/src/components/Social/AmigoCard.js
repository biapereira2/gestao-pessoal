import React from 'react';

// Adicionei a prop 'onVerPerfil'
const AmigoCard = ({ amigo, onRemover, onVerPerfil }) => {
  return (
    <div className="amigo-card">
      <div className="avatar-circle">
        {amigo.nome ? amigo.nome.charAt(0).toUpperCase() : '?'}
      </div>

      <div className="card-content-wrapper">
        <div className="card-info">
          <div className="card-name">{amigo.nome || "Usuário"}</div>
          <div className="card-status">Amigo</div>
        </div>

        <div className="card-actions">
          {/* AÇÃO AQUI: Chama a função passando o objeto amigo */}
          <button
            className="btn-outline"
            onClick={() => onVerPerfil(amigo)}
          >
            Ver Perfil
          </button>

          <button
            className="btn-danger"
            onClick={() => onRemover(amigo.id)}
          >
            Desfazer
          </button>
        </div>
      </div>
    </div>
  );
};

export default AmigoCard;