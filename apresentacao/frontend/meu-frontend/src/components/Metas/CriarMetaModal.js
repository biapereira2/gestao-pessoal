// src/components/Metas/CriarMetaModal.js
import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import { habitoService } from '../../services/habitoService';

const CriarMetaModal = ({ onClose, onSalvar, usuarioId }) => {
  const [formData, setFormData] = useState({
    descricao: '',
    tipo: 'MENSAL', // Padrão
    // 'prazo' removido daqui
    habitosIds: [],
    alertaProximoFalha: true,
  });
  const [habitosDisponiveis, setHabitosDisponiveis] = useState([]);
  const [loadingHabitos, setLoadingHabitos] = useState(false);

  useEffect(() => {
    const carregarHabitos = async () => {
        if (!usuarioId) return;
        setLoadingHabitos(true);
        try {
            const data = await habitoService.listarPorUsuario(usuarioId);
            setHabitosDisponiveis(data);
        } catch (error) {
            toast.error("Erro ao carregar hábitos disponíveis para associação.");
        } finally {
            setLoadingHabitos(false);
        }
    };
    carregarHabitos();
  }, [usuarioId]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleHabitoToggle = (habitoId) => {
    setFormData(prev => {
        const isSelected = prev.habitosIds.includes(habitoId);
        return {
            ...prev,
            habitosIds: isSelected
                ? prev.habitosIds.filter(id => id !== habitoId)
                : [...prev.habitosIds, habitoId]
        };
    });
  };

  const handleSubmit = () => {
    if (!formData.descricao || formData.habitosIds.length === 0) {
      alert("Por favor, preencha a Descrição e selecione pelo menos um Hábito.");
      return;
    }

    const dadosParaSalvar = {
        ...formData,
        usuarioId,
        habitosCompletos: 0, // Inicia em 0
        prazo: null, // Garante que o campo 'prazo' seja enviado como null ao backend
    };

    onSalvar(dadosParaSalvar);
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content-creation" onClick={e => e.stopPropagation()}>
        <button className="btn-close-absolute" onClick={onClose}>✕</button>

        <div>
          <h2 className="modal-title-creation">Criar Nova Meta</h2>
          <p className="modal-subtitle-creation">Defina o objetivo e associe hábitos.</p>
        </div>

        <div>
          <div className="form-group">
            <label>Descrição da Meta</label>
            <input
              className="input-creation"
              name="descricao"
              placeholder="Ex.: Completar Rotina de Saúde Semanal"
              value={formData.descricao}
              onChange={handleChange}
              autoFocus
            />
          </div>

          <div className="form-row">
            <div className="form-group" style={{ width: '100%' }}> {/* Ocupa a largura total */}
              <label>Tipo (Semanal/Mensal)</label>
              <select
                className="input-creation"
                name="tipo"
                value={formData.tipo}
                onChange={handleChange}
              >
                <option value="SEMANAL">Semanal</option>
                <option value="MENSAL">Mensal</option>
              </select>
            </div>
            {/* O campo Prazo Final foi removido daqui */}
          </div>

          <div className="form-group">
            <label>Hábitos Associados ({formData.habitosIds.length})</label>
            <div style={{
                maxHeight: '150px', overflowY: 'auto', border: '1px solid #E0E0E0',
                borderRadius: '12px', padding: '10px', backgroundColor: '#F7F7F8'
            }}>
                {loadingHabitos && <p style={{fontSize: '14px', color: 'var(--text-secondary)'}}>Carregando hábitos...</p>}
                {!loadingHabitos && habitosDisponiveis.length === 0 && <p style={{fontSize: '14px', color: 'var(--text-secondary)'}}>Nenhum hábito encontrado para associar. Crie hábitos primeiro.</p>}

                {habitosDisponiveis.map(habito => (
                    <div
                        key={habito.id}
                        onClick={() => handleHabitoToggle(habito.id)}
                        style={{
                            padding: '8px 10px', margin: '5px 0', borderRadius: '8px',
                            cursor: 'pointer', fontSize: '14px', fontWeight: '500',
                            backgroundColor: formData.habitosIds.includes(habito.id) ? '#e8f5e9' : 'white',
                            border: `1px solid ${formData.habitosIds.includes(habito.id) ? '#2E7D32' : '#E0E0E0'}`,
                            color: 'var(--text-primary)'
                        }}
                    >
                        {habito.nome} <span style={{fontSize: '10px', fontWeight: '700', color: 'var(--text-secondary)'}}>({habito.frequencia})</span>
                    </div>
                ))}
            </div>
          </div>

        </div>

        <button
          className="btn-primary-habito btn-block"
          onClick={handleSubmit}
          disabled={loadingHabitos}
        >
          Criar Meta
        </button>

      </div>
    </div>
  );
};

export default CriarMetaModal;