import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import DashboardLayout from '../components/DashboardLayout';
import NivelCard from '../components/Progresso/NivelCard';
import { progressoUsuarioService } from '../services/progressoUsuarioService';
import { toast } from 'react-toastify';

const ProgressoUsuario = () => {
  const { id } = useParams();
  const [progresso, setProgresso] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const carregar = async () => {
      try {
        setLoading(true);
        const data = await progressoUsuarioService.buscarPorUsuario(id);
        setProgresso(data);
      } catch (e) {
        toast.error("Erro ao carregar progresso do usuário");
      } finally {
        setLoading(false);
      }
    };

    carregar();
  }, [id]);

  return (
    <DashboardLayout>
      <div className="habitos-page" style={{ padding: '0 20px' }}>
        <h1 style={{ fontWeight: 800 }}>Seu Progresso</h1>
        <p style={{ color: 'var(--text-secondary)' }}>
          Acompanhe sua evolução e conquistas
        </p>

        {loading && <p>Carregando progresso...</p>}

        {!loading && progresso && (
          <div style={{ marginTop: '25px', display: 'flex', gap: '20px', flexWrap: 'wrap' }}>
            <NivelCard
              nivel={progresso.nivelAtual}
              pontos={progresso.pontos}
              pontosParaProximo={progresso.pontosProximoNivel}
            />

            <div className="habito-card">
              <h3>Total de Check-ins</h3>
              <div style={{ fontSize: '28px', fontWeight: 800 }}>
                {progresso.totalCheckins}
              </div>
            </div>

            <div className="habito-card">
              <h3>Badges Desbloqueadas</h3>
              <div style={{ fontSize: '28px', fontWeight: 800 }}>
                {progresso.totalBadges}
              </div>
            </div>
          </div>
        )}
      </div>
    </DashboardLayout>
  );
};

export default ProgressoUsuario;
