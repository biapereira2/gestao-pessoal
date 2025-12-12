import React, { useState } from 'react';
import '../../css/habitos.css';

const CriarHabitoModal = ({ onClose, onSalvar, usuarioId }) => {
  const [formData, setFormData] = useState({
    nome: '',
    descricao: '',
    categoria: '',
    frequencia: '',
    usuarioId: usuarioId
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = () => {
    if (!formData.nome || !formData.frequencia) {
      alert("Por favor, preencha o Nome e a Frequência.");
      return;
    }
    onSalvar(formData);
  };

  return (
    <div className="modal-overlay" onClick={onClose}>

      <div className="modal-content-creation" onClick={e => e.stopPropagation()}>

        <button className="btn-close-absolute" onClick={onClose}>✕</button>

        <div>
          <h2 className="modal-title-creation">Novo Hábito</h2>
          <p className="modal-subtitle-creation">Defina uma nova meta para sua rotina.</p>
        </div>

        <div>
          <div className="form-group">
            <label>Nome do hábito</label>
            <input
              className="input-creation"
              name="nome"
              placeholder="Ex.: Ler 10 páginas"
              value={formData.nome}
              onChange={handleChange}
              autoFocus
            />
          </div>
          <div className="form-group">
            <label>Descrição (Opcional)</label>
            <input
              className="input-creation"
              name="descricao"
              placeholder="Detalhes sobre como realizar..."
              value={formData.descricao}
              onChange={handleChange}
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Categoria</label>
              <input
                className="input-creation"
                name="categoria"
                placeholder="Ex: Saúde"
                value={formData.categoria}
                onChange={handleChange}
              />
            </div>

            <div className="form-group">
              <label>Frequência</label>
              <input
                className="input-creation"
                name="frequencia"
                placeholder="Ex: Diária"
                value={formData.frequencia}
                onChange={handleChange}
              />
            </div>
          </div>
        </div>

        <button
          className="btn-primary-habito btn-block"
          onClick={handleSubmit}
        >
          Criar Hábito
        </button>

      </div>
    </div>
  );
};

export default CriarHabitoModal;