import React, { useState, useEffect } from 'react';
import { desafioService } from '../../services/desafioService';
import { toast } from 'react-toastify'; // É crucial importar o toast aqui

// Adicionamos 'onDesafioEncerrado' nas props para recarregar a lista principal
const DesafioCard = ({ desafio, usuarioId, onDesafioEncerrado }) => {
  const [progressoData, setProgressoData] = useState(null);
  const [loadingProgresso, setLoadingProgresso] = useState(false);
  const [expandido, setExpandido] = useState(false);

  useEffect(() => {
    const carregarProgresso = async () => {
      setLoadingProgresso(true);
      // Teste 4: Acompanhar Progresso
      const data = await desafioService.acompanharProgresso(desafio.id);
      setProgressoData(data);
      setLoadingProgresso(false);
    };

    if (desafio && desafio.id) {
      carregarProgresso();
    }
  }, [desafio]);

  // Função para encerrar o desafio (Teste 5)
  const handleEncerrarDesafio = async () => {
    if (!window.confirm("Tem certeza que deseja encerrar este desafio? Esta ação é irreversível.")) {
        return;
    }
    try {
      // Chamada para o Service: POST /desafios/{desafioId}/encerrar?criadorId={criadorId}
      await desafioService.encerrarDesafio(desafio.id, usuarioId);
      toast.success("Desafio encerrado com sucesso! A lista será atualizada.");

      // Chama a função passada pelo componente pai (Desafios.js) para recarregar a lista
      if (onDesafioEncerrado) {
        onDesafioEncerrado();
      }
    } catch (error) {
      console.error("Erro ao encerrar desafio:", error);
      toast.error("Erro ao encerrar desafio: " + error.message);
    }
  };


  if (!desafio) return null;

  // Lógica de progresso
  const meuProgresso = progressoData ? progressoData.find(p => p.participanteId === usuarioId) : null;
  const progressoPct = meuProgresso && meuProgresso.totalHabitos > 0
    ? Math.round((meuProgresso.habitosConcluidos / meuProgresso.totalHabitos) * 100)
    : 0;

  const isCriador = desafio.criadorId === usuarioId;
  const statusCor = progressoPct === 100 ? '#2E7D32' : '#1976D2';

  // Componente interno para mostrar o progresso detalhado
  const DetalhesProgresso = () => (
    <div style={{ marginTop: '10px', padding: '10px', borderTop: '1px solid #E0E0E0' }}>
      <h4 style={{ fontSize: '14px', fontWeight: 700, marginBottom: '5px' }}>Progresso dos Participantes:</h4>
      {loadingProgresso && <p style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>Atualizando progresso...</p>}

      {progressoData && progressoData.map((p, index) => (
        <div key={index} style={{ marginBottom: '8px' }}>
          <p style={{ fontSize: '13px', fontWeight: 600, color: p.participanteId === usuarioId ? statusCor : 'var(--text-primary)' }}>
            {p.nomeParticipante} (Você {p.participanteId === usuarioId ? 'SIM' : 'NÃO'})
          </p>
          <p style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>
            {p.habitosConcluidos} de {p.totalHabitos} hábitos (
            {p.totalHabitos > 0 ? Math.round((p.habitosConcluidos / p.totalHabitos) * 100) : 0}%)
          </p>
          <div style={{ height: '5px', width: '100%', backgroundColor: '#E0E0E0', borderRadius: '4px', overflow: 'hidden' }}>
            <div
              style={{
                width: `${p.totalHabitos > 0 ? Math.round((p.habitosConcluidos / p.totalHabitos) * 100) : 0}%`,
                height: '100%',
                backgroundColor: p.participanteId === usuarioId ? statusCor : '#64B5F6',
                transition: 'width 0.5s'
              }}
            ></div>
          </div>
        </div>
      ))}
    </div>
  );

  return (
    <div className="desafio-card">
      <div className="desafio-header">
        <h3 className="desafio-title">{desafio.nome}</h3>
        <span className="desafio-status" style={{ backgroundColor: statusCor }}>
          {desafio.status} {isCriador ? '(Criador)' : ''}
        </span>
      </div>

      <p className="desafio-detail">
        Encerra em: {new Date(desafio.dataFim).toLocaleDateString()}
      </p>

      {meuProgresso && (
        <div className="desafio-progresso-bar">
          <p style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>
            Seu Progresso: {meuProgresso.habitosConcluidos} de {meuProgresso.totalHabitos} ({progressoPct}%)
          </p>
          <div style={{ height: '8px', width: '100%', backgroundColor: '#E0E0E0', borderRadius: '4px', overflow: 'hidden', marginTop: '5px' }}>
            <div
              style={{
                width: `${progressoPct}%`, height: '100%',
                backgroundColor: statusCor, transition: 'width 0.5s'
              }}
            ></div>
          </div>
        </div>
      )}

      {expandido && <DetalhesProgresso />}

      <div className="desafio-actions">
        <button className="btn-outline" onClick={() => setExpandido(!expandido)}>
          {expandido ? 'Ocultar Detalhes' : 'Ver Progresso Completo'}
        </button>

        {desafio.status === 'ATIVO' && (
            <button
                className="btn-danger"
                onClick={handleEncerrarDesafio}
            >
                Encerrar Desafio (FORÇADO)
            </button>
        )}
      </div>
    </div>
  );
};

export default DesafioCard;