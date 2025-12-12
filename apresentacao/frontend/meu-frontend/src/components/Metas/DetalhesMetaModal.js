// src/components/Metas/DetalhesMetaModal.js
import React, { useEffect, useState } from 'react';
import { habitoService } from '../../services/habitoService';
import { toast } from 'react-toastify';

const DetalhesMetaModal = ({ onClose, meta }) => {
  const [habitos, setHabitos] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const carregarHabitos = async () => {
      if (!meta || !meta.habitosIds || meta.habitosIds.length === 0) {
        setHabitos([]);
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        const dados = await Promise.all(
          meta.habitosIds.map(id => habitoService.obterPorId(id))
        );
        setHabitos(dados);
      } catch (error) {
        console.error("Erro ao carregar hábitos:", error);
        toast.error("Não foi possível carregar os hábitos associados.");
      } finally {
        setLoading(false);
      }
    };

    carregarHabitos();
  }, [meta]);

  if (!meta || loading) return (
    <div className="modal-overlay">
      <div className="modal-content-creation">
        <p>Carregando...</p>
      </div>
    </div>
  );

  const progresso = meta.quantidade > 0
    ? Math.round((meta.habitosCompletos / meta.quantidade) * 100)
    : 0;

  const statusCor = progresso === 100
    ? '#2E7D32'
    : (meta.alertaProximoFalha ? '#FFB300' : 'var(--text-primary)');

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content-creation" onClick={e => e.stopPropagation()}>
        <button className="btn-close-absolute" onClick={onClose}>✕</button>

        <div style={{
            display: 'flex', flexDirection: 'column', alignItems: 'center',
            marginBottom: '20px', textAlign: 'center'
        }}>
            <h2 className="modal-title-creation" style={{ marginBottom: '5px', color: statusCor }}>
                {meta.descricao}
            </h2>

            <span style={{
                backgroundColor: statusCor, padding: '4px 12px',
                borderRadius: '20px', fontSize: '12px', fontWeight: '700',
                textTransform: 'uppercase', letterSpacing: '0.5px',
                color: '#FFFFFF'
            }}>
                {meta.tipo} - {progresso}% Completa
            </span>
        </div>

        <div className="modal-body-simple" style={{
            borderTop: '1px dashed #E0E0E0',
            paddingTop: '25px',
            textAlign: 'left'
        }}>

          <div style={{ display: 'flex', gap: '30px', marginBottom: '20px' }}>
            <div style={{ flex: 1 }}>
              <label style={{
                  display: 'block', fontSize: '11px', fontWeight: '700',
                  color: 'var(--text-secondary)', textTransform: 'uppercase',
                  marginBottom: '5px'
              }}>
                  HÁBITOS COMPLETOS
              </label>
              <div style={{ fontSize: '16px', fontWeight: '600', color: statusCor }}>
                  {meta.habitosCompletos} de {meta.quantidade}
              </div>
            </div>

            <div style={{ flex: 1 }}>
              <label style={{
                  display: 'block', fontSize: '11px', fontWeight: '700',
                  color: 'var(--text-secondary)', textTransform: 'uppercase',
                  marginBottom: '5px'
              }}>
                  PRAZO
              </label>
              <div style={{ fontSize: '16px', fontWeight: '600', color: 'var(--text-primary)' }}>
                  {meta.prazo ? new Date(meta.prazo).toLocaleDateString() : 'Sem Prazo'}
              </div>
            </div>
          </div>

          <div>
           <label style={{
               display: 'block', fontSize: '11px', fontWeight: '700',
               color: 'var(--text-secondary)', textTransform: 'uppercase',
               marginBottom: '8px', letterSpacing: '0.5px'
           }}>
               HÁBITOS ASSOCIADOS
           </label>
           <ul style={{
               fontSize: '14px', color: 'var(--text-primary)',
               lineHeight: '1.6', paddingLeft: '20px', margin: 0
           }}>
               {habitos.length > 0
                 ? habitos.map(h => <li key={h.id}>{h.nome}</li>)
                 : <li>Nenhum hábito associado</li>
               }
           </ul>
         </div>

        </div>

        <button
          className="btn-outline btn-block"
          onClick={onClose}
          style={{ marginTop: '25px' }}
        >
          Fechar
        </button>

      </div>
    </div>
  );
};

export default DetalhesMetaModal;
