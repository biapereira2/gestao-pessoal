// src/components/Metas/EditarMetaModal.js
import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import { habitoService } from '../../services/habitoService';

const EditarMetaModal = ({ onClose, onSalvar, meta }) => {
  const [formData, setFormData] = useState({
    descricao: '',
    tipo: 'MENSAL', // valor padrão
    habitosIds: [],
    alertaProximoFalha: false,
    habitosCompletos: 0
  });

  const [habitosDisponiveis, setHabitosDisponiveis] = useState([]);
  const [loadingHabitos, setLoadingHabitos] = useState(false);

  useEffect(() => {
    if (meta) {
      setFormData({
        descricao: meta.descricao || '',
        tipo: meta.tipo ? meta.tipo.toUpperCase() : 'MENSAL', // garante string compatível com select
        habitosIds: meta.habitosIds || [],
        alertaProximoFalha: meta.alertaProximoFalha || false,
        habitosCompletos: meta.habitosCompletos || 0,
      });
    }

    const carregarHabitos = async () => {
      if (!meta || !meta.usuarioId) return;
      setLoadingHabitos(true);
      try {
        const data = await habitoService.listarPorUsuario(meta.usuarioId);
        setHabitosDisponiveis(data);
      } catch (error) {
        toast.error("Erro ao carregar hábitos disponíveis para edição.");
      } finally {
        setLoadingHabitos(false);
      }
    };

    if (meta) carregarHabitos();
  }, [meta]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    const val = type === 'checkbox' ? checked : value;

    setFormData(prev => ({
      ...prev,
      [name]: val
    }));
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
      alert("Descrição e pelo menos um Hábito são obrigatórios.");
      return;
    }

    const dadosParaSalvar = {
      ...formData,
      quantidade: formData.habitosIds.length,
      habitosCompletos: meta.habitosCompletos || 0,
      prazo: null, // envia null
      alertaProximoFalha: false,
      tipo: formData.tipo.toUpperCase(), // garante compatibilidade com backend
    };

    onSalvar(meta.id, dadosParaSalvar);
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content-creation" onClick={e => e.stopPropagation()}>

        <button className="btn-close-absolute" onClick={onClose}>✕</button>

        <div>
          <h2 className="modal-title-creation">Editar Meta</h2>
          <p className="modal-subtitle-creation">Ajuste as informações e hábitos da sua meta.</p>
        </div>

        <div>
          <div className="form-group">
            <label>Descrição da Meta</label>
            <input
              className="input-creation"
              name="descricao"
              value={formData.descricao}
              onChange={handleChange}
            />
          </div>

          <div className="form-row">
            <div className="form-group" style={{ width: '100%' }}>
              <label>Tipo (Semanal/Mensal)</label>
              <select
                className="input-creation"
                name="tipo"
                value={formData.tipo}
                disabled // <-- aqui congela o select
              >
                <option value="SEMANAL">Semanal</option>
                <option value="MENSAL">Mensal</option>
              </select>
            </div>

          </div>

          <div className="form-group">
            <label>Hábitos Associados ({formData.habitosIds.length} de {habitosDisponiveis.length})</label>
            <div style={{
                maxHeight: '150px', overflowY: 'auto', border: '1px solid #E0E0E0',
                borderRadius: '12px', padding: '10px', backgroundColor: '#F7F7F8'
            }}>
                {loadingHabitos && <p style={{fontSize: '14px', color: 'var(--text-secondary)'}}>Carregando hábitos...</p>}
                {!loadingHabitos && habitosDisponiveis.length === 0 && <p style={{fontSize: '14px', color: 'var(--text-secondary)'}}>Nenhum hábito encontrado.</p>}

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
          Salvar Alterações
        </button>

      </div>
    </div>
  );
};

export default EditarMetaModal;
