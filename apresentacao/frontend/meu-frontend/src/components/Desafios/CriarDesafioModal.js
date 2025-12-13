import React, { useState } from 'react';
import { toast } from 'react-toastify';
import { desafioService } from '../../services/desafioService'; // Embora não usado, é bom ter

const CriarDesafioModal = ({ onClose, onSalvar, habitosDisponiveis }) => {
  const [formData, setFormData] = useState({
    nome: '',
    dataFim: '',
    emailsConvidados: '', // String de emails separados por vírgula
    habitosIds: [],
  });
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
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

  const handleSubmit = async () => {
    if (!formData.nome || !formData.dataFim || formData.habitosIds.length === 0) {
      toast.error("Nome, Data Fim e pelo menos um Hábito são obrigatórios.");
      return;
    }

    // Processa a string de emails
    const emailsArray = formData.emailsConvidados
      .split(',')
      .map(email => email.trim())
      .filter(email => email.length > 0);

    // Dados prontos para o Backend (O onSalvar adiciona o criadorId)
    const dadosParaSalvar = {
      nome: formData.nome,
      dataFim: formData.dataFim,
      habitosIds: formData.habitosIds,
      emailsConvidados: emailsArray,
      // dataInicio será definida no Backend
    };

    setLoading(true);
    try {
      await onSalvar(dadosParaSalvar);
    } catch(error) {
      // O erro é tratado na página principal (Desafios.js)
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content-creation" onClick={e => e.stopPropagation()}>

        <button className="btn-close-absolute" onClick={onClose}>✕</button>

        <div>
          <h2 className="modal-title-creation">Criar Novo Desafio</h2>
          <p className="modal-subtitle-creation">Defina os hábitos e convide participantes.</p>
        </div>

        <div style={{ padding: '0 10px' }}>
          <div className="form-group">
            <label>Nome do Desafio</label>
            <input
              className="input-creation"
              name="nome"
              value={formData.nome}
              onChange={handleChange}
            />
          </div>

          <div className="form-row">
            <div className="form-group" style={{ width: '50%' }}>
              <label>Data de Encerramento</label>
              <input
                className="input-creation"
                name="dataFim"
                type="date"
                value={formData.dataFim}
                onChange={handleChange}
              />
            </div>
            <div className="form-group" style={{ width: '50%' }}>
              <label>Emails para Convite (Separar por vírgula)</label>
              <input
                className="input-creation"
                name="emailsConvidados"
                placeholder="amigo1@teste.com, amigo2@teste.com"
                value={formData.emailsConvidados}
                onChange={handleChange}
              />
            </div>
          </div>

          <div className="form-group">
            <label>Hábitos a serem rastreados ({formData.habitosIds.length} selecionados)</label>
            <div style={{ maxHeight: '150px', overflowY: 'auto', border: '1px solid #E0E0E0', borderRadius: '12px', padding: '10px', backgroundColor: '#F7F7F8' }}>
                {habitosDisponiveis.length === 0 && <p style={{fontSize: '14px', color: 'var(--text-secondary)'}}>Nenhum hábito disponível para seleção. Crie um primeiro.</p>}

                {habitosDisponiveis.map(habito => (
                    <div
                        key={habito.id}
                        onClick={() => handleHabitoToggle(habito.id)}
                        style={{
                            padding: '8px 10px', margin: '5px 0', borderRadius: '8px',
                            cursor: 'pointer', fontSize: '14px', fontWeight: '500',
                            backgroundColor: formData.habitosIds.includes(habito.id) ? '#e8f5e9' : 'white',
                            border: `1px solid ${formData.habitosIds.includes(habito.id) ? '#2E7D32' : '#E0E0E0'}`,
                            color: 'var(--text-primary)', display: 'flex', justifyContent: 'space-between'
                        }}
                    >
                        {habito.nome}
                        <span style={{fontSize: '10px', fontWeight: '700', color: 'var(--text-secondary)'}}>({habito.frequencia})</span>
                    </div>
                ))}
            </div>
          </div>

        </div>

        <button
          className="btn-primary-habito btn-block"
          onClick={handleSubmit}
          disabled={loading}
        >
          {loading ? 'Criando...' : 'Criar Desafio'}
        </button>

      </div>
    </div>
  );
};

export default CriarDesafioModal;