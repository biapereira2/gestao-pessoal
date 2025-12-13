const NivelCard = ({ nivel, pontos, pontosParaProximo }) => {
  const progresso = Math.min(
    Math.round((pontos / pontosParaProximo) * 100),
    100
  );

  return (
    <div className="habito-card">
      <h3 style={{ fontWeight: 800 }}>Nível {nivel}</h3>

      <p style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>
        {pontos} / {pontosParaProximo} pontos
      </p>

      <div style={{
        height: '10px',
        backgroundColor: '#E0E0E0',
        borderRadius: '6px',
        overflow: 'hidden',
        marginTop: '8px'
      }}>
        <div style={{
          width: `${progresso}%`,
          height: '100%',
          backgroundColor: '#1976d2',
          transition: 'width 0.5s'
        }} />
      </div>

      <p style={{
        fontSize: '12px',
        marginTop: '6px',
        color: 'var(--text-secondary)'
      }}>
        {pontosParaProximo - pontos} pontos para o próximo nível
      </p>
    </div>
  );
};

export default NivelCard;
