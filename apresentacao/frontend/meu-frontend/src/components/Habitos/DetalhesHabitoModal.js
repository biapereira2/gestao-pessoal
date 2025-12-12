import React from 'react';
import '../../css/habitos.css';

const DetalhesHabitoModal = ({ onClose, habito }) => {
  if (!habito) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>

      <div className="modal-content-creation" onClick={e => e.stopPropagation()}>

        <button className="btn-close-absolute" onClick={onClose}>✕</button>

        <div style={{
            display: 'flex', flexDirection: 'column', alignItems: 'center',
            marginBottom: '20px', textAlign: 'center'
        }}>
            <div style={{
                width: '70px', height: '70px', borderRadius: '50%',
                backgroundColor: 'var(--text-primary)', color: 'white',
                display: 'flex', alignItems: 'center', justifyContent: 'center',
                fontSize: '28px', fontWeight: 'bold', marginBottom: '10px'
            }}>
                {habito.nome.charAt(0).toUpperCase()}
            </div>

            <h2 className="modal-title-creation" style={{marginBottom: '5px'}}>
                {habito.nome}
            </h2>

            <span style={{
                backgroundColor: '#F0F2F5', padding: '4px 12px',
                borderRadius: '20px', fontSize: '12px', fontWeight: '700',
                textTransform: 'uppercase', letterSpacing: '0.5px',
                border: '1px solid #E0E0E0', color: 'var(--text-secondary)'
            }}>
                {habito.categoria || 'GERAL'}
            </span>
        </div>

        <div className="modal-body-simple" style={{
            borderTop: '1px dashed #E0E0E0',
            paddingTop: '25px',
            textAlign: 'left'
        }}>
          <div style={{marginBottom: '20px'}}>
            <label style={{
                display: 'block', fontSize: '11px', fontWeight: '700',
                color: 'var(--text-secondary)', textTransform: 'uppercase',
                marginBottom: '8px', letterSpacing: '0.5px'
            }}>
                DESCRIÇÃO
            </label>
            <div style={{
                fontSize: '15px', color: 'var(--text-primary)',
                lineHeight: '1.6', backgroundColor: '#FAFAFA',
                padding: '15px', borderRadius: '8px', border: '1px solid #EEEEEE'
            }}>
                {habito.descricao || "Nenhuma descrição informada."}
            </div>
          </div>
          <div style={{display: 'flex', gap: '30px'}}>

            <div style={{flex: 1}}>
              <label style={{
                  display: 'block', fontSize: '11px', fontWeight: '700',
                  color: 'var(--text-secondary)', textTransform: 'uppercase',
                  marginBottom: '5px'
              }}>
                  FREQUÊNCIA
              </label>
              <div style={{fontSize: '16px', fontWeight: '600', color: 'var(--text-primary)'}}>
                  {habito.frequencia}
              </div>
            </div>

            <div style={{flex: 1}}>
                <label style={{
                    display: 'block', fontSize: '11px', fontWeight: '700',
                    color: 'var(--text-secondary)', textTransform: 'uppercase',
                    marginBottom: '5px'
                }}>
                    STATUS
                </label>
                <div style={{
                    fontSize: '16px', fontWeight: '600',
                    color: '#2E7D32', display: 'flex', alignItems: 'center', gap: '5px'
                }}>
                    <span style={{width:'8px', height:'8px', borderRadius:'50%', background:'#2E7D32'}}></span>
                    Ativo
                </div>
            </div>

          </div>

        </div>

        <button
          className="btn-outline btn-block"
          onClick={onClose}
          style={{marginTop: '25px'}}
        >
          Fechar
        </button>

      </div>
    </div>
  );
};

export default DetalhesHabitoModal;