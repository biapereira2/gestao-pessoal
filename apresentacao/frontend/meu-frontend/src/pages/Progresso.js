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

        // ✅ método correto do service
        const data = await progressoUsuarioService.visualizar(id);

        console.log('Progresso carregado:', data);
        setProgresso(data);
      } catch (e) {
        console.error(e);
        toast.error('Erro ao carregar progresso do usuário');
      } finally {
        setLoading(false);
      }
    };

    if (id) {
      carregar();
    }
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
          <div
            style={{
              marginTop: '25px',
              display: 'flex',
              gap: '20px',
              flexWrap: 'wrap'
            }}
          >
            {/* ✅ Campos alinhados ao backend */}
            <NivelCard
              nivel={progresso.nivel}
              pontos={progresso.pontos}
              pontosParaProximo={progresso.pontosFaltantes}
            />

            {/* 🔒 Reservado para evolução futura */}
            <div className="habito-card">
              <h3>Total de Check-ins</h3>
              <div style={{ fontSize: '28px', fontWeight: 800 }}>
                0
              </div>
            </div>

            <div className="habito-card">
              <h3>Badges Desbloqueadas</h3>
              <div style={{ fontSize: '28px', fontWeight: 800 }}>
                0
              </div>
            </div>
          </div>
        )}
      </div>
    </DashboardLayout>
  );
};

export default ProgressoUsuario;
