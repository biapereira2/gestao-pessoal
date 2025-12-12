// src/components/Alertas/AlertaCard.js
import React from 'react';
import '../../css/alertas.css';
import format from 'date-fns/format';

const AlertaCard = ({ alerta, onVerDetalhes, onEditar, onRemover }) => {
  if (!alerta) return null;

  // Garante que new Date() não crie um objeto inválido se a data não estiver presente
  const dataValida = alerta.dataDisparo ? new Date(alerta.dataDisparo) : null;

  const isDisparado = alerta.disparado || (dataValida && dataValida < new Date());
  const statusCor = isDisparado ? '#D32F2F' : '#2E7D32';
  const statusTexto = isDisparado ? 'Disparado' : 'Ativo';

  let dataFormatada = 'N/A';
  if (dataValida && !isNaN(dataValida)) {
      try {
          // Usa o objeto de data já criado e validado
          dataFormatada = format(dataValida, 'dd/MM/yyyy');
      } catch (error) {
          console.error("Erro ao formatar data no Card:", error);
          dataFormatada = alerta.dataDisparo; // Se falhar, usa a string do banco
      }
  } else if (alerta.dataDisparo) {
      // Se new Date() falhou (dataValida é null/NaN), mas a string existe, mostra a string bruta do DB
      dataFormatada = alerta.dataDisparo;
  }

  return (
    <div className="meta-card" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
      <div className="meta-info">
        <div className="meta-title">{alerta.titulo}</div>
        <div className="meta-meta">
          Categoria: {alerta.categoria || 'Geral'} <span className="meta-separator"></span>
          Data de Disparo: {dataFormatada} <span className="meta-separator"></span>
          <span style={{ color: statusCor, fontWeight: 600 }}>{statusTexto}</span>
        </div>
      </div>
      <div className="meta-actions">
        <button className="btn-outline" onClick={() => onVerDetalhes(alerta)}>Ver</button>
        <button className="btn-outline" onClick={() => onEditar(alerta)}>Editar</button>
        <button className="btn-danger" onClick={() => onRemover(alerta)}>Apagar</button>
      </div>
    </div>
  );
};

export default AlertaCard;