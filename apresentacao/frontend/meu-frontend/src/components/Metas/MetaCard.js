import React from 'react';

const MetaCard = ({ meta, onRemover, onEditar, onVerDetalhes }) => {
  if (!meta) return null;

  const progresso = meta.quantidade > 0
    ? Math.round((meta.habitosCompletos / meta.quantidade) * 100)
    : 0;

  const hoje = new Date();
  const prazo = meta.prazo ? new Date(meta.prazo) : null;
  const prazoTerminou = prazo && prazo < hoje;

  // Define dias de alerta conforme o tipo de meta
  const diasAlerta = meta.tipo === 'MENSAL' ? 7 : 2;

  let diffDias = null;
  if (prazo) {
    const diffTime = prazo - hoje;
    diffDias = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  }

  // Lógica do status
  let statusBadge = meta.tipo === 'MENSAL' ? 'Mensal' : 'Semanal';
  let statusCor = '#030213';
  let statusSecundario = 'Em Andamento';

  if (progresso === 100) {
    statusSecundario = 'Concluído';
    statusCor = '#2E7D32';
  } else if (prazoTerminou && progresso < 100) {
    statusSecundario = 'Falha';
    statusCor = '#D32F2F';
  } else if (diffDias !== null && diffDias <= diasAlerta) {
    statusSecundario = 'Atenção!';
    statusCor = '#FFB300';
  }

  // Mensagem de prazo
  let prazoMensagem = '';
  if (progresso < 100) {
    if (prazoTerminou) {
      prazoMensagem = `Prazo terminado em ${prazo.toLocaleDateString()}.`;
    } else if (prazo) {
      prazoMensagem = `Faltam ${meta.quantidade - meta.habitosCompletos} hábitos e o prazo termina em ${diffDias} dias!`;
    } else {
      prazoMensagem = `Faltam ${meta.quantidade - meta.habitosCompletos} hábitos.`;
    }
  }

  return (
    <div className="habito-card">
      <div className="habito-info">
        <div className="habito-title" style={{ color: statusCor }}>
          {meta.descricao || `Meta ${meta.id.substring(0, 4).toUpperCase()}`}
        </div>
        <div className="habito-meta" style={{ marginBottom: '10px' }}>
          <span
            style={{
              backgroundColor: '#F0F2F5', padding: '4px 10px',
              borderRadius: '5px', fontSize: '10px', fontWeight: '700',
              textTransform: 'uppercase', letterSpacing: '0.5px',
              border: '1px solid #E0E0E0', color: 'var(--text-secondary)'
            }}
          >
            {statusBadge}
          </span>
          <span
            style={{
              backgroundColor: statusCor, padding: '4px 10px',
              borderRadius: '5px', fontSize: '10px', fontWeight: '700',
              textTransform: 'uppercase', letterSpacing: '0.5px',
              color: '#FFFFFF'
            }}
          >
            {statusSecundario}
          </span>
        </div>

        <div style={{ marginBottom: '10px' }}>
          <p style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>
            {meta.habitosCompletos} de {meta.quantidade} hábitos ({progresso}%)
          </p>
          <div
            style={{
              height: '8px', width: '100%', backgroundColor: '#E0E0E0',
              borderRadius: '4px', overflow: 'hidden', marginTop: '5px'
            }}
          >
            <div
              style={{
                width: `${progresso}%`, height: '100%',
                backgroundColor: progresso === 100 ? '#2E7D32' : statusCor,
                transition: 'width 0.5s'
              }}
            ></div>
          </div>
        </div>

        {prazoMensagem && (
          <p style={{ fontSize: '12px', fontWeight: '600', color: statusCor }}>
            {prazoMensagem}
          </p>
        )}
      </div>

      <div className="habito-actions" style={{ justifyContent: 'flex-start' }}>
        <button className="btn-outline" onClick={() => onVerDetalhes(meta)}>
          Ver Detalhes
        </button>
        <button className="btn-outline" onClick={() => onEditar(meta)}>
          Editar
        </button>
        <button className="btn-danger" onClick={() => onRemover(meta)}>
          Apagar
        </button>
      </div>
    </div>
  );
};

export default MetaCard;
