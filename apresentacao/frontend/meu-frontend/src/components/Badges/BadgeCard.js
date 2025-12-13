const BadgeCard = ({ badge, onClick }) => {
  const desbloqueada = badge.conquistada;

  return (
    <div
      className="habito-card"
      onClick={() => onClick(badge)}
      style={{
        cursor: 'pointer',
        opacity: desbloqueada ? 1 : 0.4
      }}
    >
      <h3>{badge.nome}</h3>

      <p style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>
        {badge.descricao}
      </p>

      <span style={{
        marginTop: '10px',
        display: 'inline-block',
        fontSize: '11px',
        fontWeight: 700,
        color: desbloqueada ? '#2E7D32' : '#999'
      }}>
        {desbloqueada ? 'Conquistada' : 'Bloqueada'}
      </span>
    </div>
  );
};

export default BadgeCard;
