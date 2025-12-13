import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import DashboardLayout from '../components/DashboardLayout';
import BadgeCard from '../components/Badges/BadgeCard';
import { badgeService } from '../services/badgeService';
import { toast } from 'react-toastify';

const Badges = () => {
  const { id } = useParams();

  const [conquistadas, setConquistadas] = useState([]);
  const [disponiveis, setDisponiveis] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const carregarBadges = async () => {
      try {
        setLoading(true);

        const response = await badgeService.listarTodas(id);

        setConquistadas(response.conquistadas || []);
        setDisponiveis(response.disponiveis || []);
      } catch (e) {
        toast.error(e.message);
      } finally {
        setLoading(false);
      }
    };

    carregarBadges();
  }, [id]);

  return (
    <DashboardLayout>
      <div className="habitos-page" style={{ padding: '0 20px' }}>
        <h1 style={{ fontWeight: 800 }}>Badges</h1>
        <p style={{ color: 'var(--text-secondary)' }}>
          Conquistas e objetivos da sua jornada
        </p>

        {loading && <p style={{ marginTop: 20 }}>Carregando badges...</p>}

        {!loading && (
          <>
            {/* ================= CONQUISTADAS ================= */}
            <section style={{ marginTop: 30 }}>
              <h2 style={{ marginBottom: 15 }}>🏆 Conquistadas</h2>

              {conquistadas.length === 0 && (
                <p style={{ color: 'var(--text-secondary)' }}>
                  Você ainda não conquistou nenhuma badge.
                </p>
              )}

              <div
                style={{
                  display: 'grid',
                  gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))',
                  gap: '20px'
                }}
              >
                {conquistadas.map(badge => (
                  <BadgeCard
                    key={badge.id}
                    badge={badge}
                    onClick={() => {}}
                  />
                ))}
              </div>
            </section>

            {/* ================= DISPONÍVEIS ================= */}
            <section style={{ marginTop: 40 }}>
              <h2 style={{ marginBottom: 15 }}>🔒 Disponíveis</h2>

              {disponiveis.length === 0 && (
                <p style={{ color: 'var(--text-secondary)' }}>
                  Nenhuma badge disponível no momento.
                </p>
              )}

              <div
                style={{
                  display: 'grid',
                  gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))',
                  gap: '20px'
                }}
              >
                {disponiveis.map(badge => (
                  <BadgeCard
                    key={badge.id}
                    badge={badge}
                    onClick={() => {}}
                  />
                ))}
              </div>
            </section>
          </>
        )}
      </div>
    </DashboardLayout>
  );
};

export default Badges;
