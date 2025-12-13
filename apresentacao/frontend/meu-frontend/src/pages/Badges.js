import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import DashboardLayout from '../components/DashboardLayout';
import BadgeCard from '../components/Badges/BadgeCard';
import { badgeService } from '../services/badgeService';
import { toast } from 'react-toastify';

const Badges = () => {
  const { id } = useParams();
  const [badges, setBadges] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const carregar = async () => {
      try {
        setLoading(true);
        const data = await badgeService.listarPorUsuario(id);
        setBadges(data);
      } catch {
        toast.error("Erro ao carregar badges");
      } finally {
        setLoading(false);
      }
    };

    carregar();
  }, [id]);

  return (
    <DashboardLayout>
      <div className="habitos-page" style={{ padding: '0 20px' }}>
        <h1 style={{ fontWeight: 800 }}>Badges</h1>
        <p style={{ color: 'var(--text-secondary)' }}>
          Conquistas desbloqueadas ao longo da sua jornada
        </p>

        <div
          style={{
            marginTop: '25px',
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))',
            gap: '20px'
          }}
        >
          {loading && <p>Carregando badges...</p>}
          {!loading && badges.map(b => (
            <BadgeCard key={b.id} badge={b} onClick={() => {}} />
          ))}
        </div>
      </div>
    </DashboardLayout>
  );
};

export default Badges;
