import React, { useState } from 'react';
import { toast } from 'react-toastify';

const ConviteCard = ({ convite, onAceitar }) => {
  const [loading, setLoading] = useState(false);

  if (!convite) return null;

  const handleAceitar = async () => {
    setLoading(true);
    try {
      await onAceitar(convite.id);
    } catch(error) {
      // Erro é tratado na página principal
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="convite-card">
      <div className="convite-info">
        <div className="convite-title">
          Convidado para o Desafio: <strong>{convite.nomeDesafio || 'Sem Nome'}</strong>
        </div>
        <p className="convite-detail">
          Criado por: {convite.nomeCriador || 'Usuário Desconhecido'}
        </p>
        <p className="convite-detail">
          Em: {new Date(convite.dataCriacao).toLocaleDateString()}
        </p>
      </div>
      <div className="convite-actions">
        <button className="btn-success" onClick={handleAceitar} disabled={loading}>
          {loading ? 'Aceitando...' : 'Aceitar Convite'}
        </button>
      </div>
    </div>
  );
};

export default ConviteCard;