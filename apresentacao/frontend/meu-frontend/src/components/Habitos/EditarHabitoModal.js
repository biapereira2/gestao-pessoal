import React, { useState, useEffect } from 'react';
import '../../css/habitos.css';

const EditarHabitoModal = ({ onClose, onSalvar, habito }) => {
  const [formData, setFormData] = useState({
    nome: '',
    descricao: '',
    categoria: '',
    frequencia: '',
    usuarioId: ''
  });

  useEffect(() => {
    if (habito) {
      setFormData({
        nome: habito.nome || '',
        descricao: habito.descricao || '',
        categoria: habito.categoria || '',
        frequencia: habito.frequencia || '',
        usuarioId: habito.usuarioId || ''
      });
    }
  }, [habito]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = () => {
    if (!formData.nome || !formData.frequencia) {
      alert("Nome e Frequência são obrigatórios.");
      return;
    }
    onSalvar(habito.id, formData);
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content-creation" onClick={e => e.stopPropagation()}>

        <button className="btn-close-absolute" onClick={onClose}>✕</button>

        <div>
          <h2 className="modal-title-creation">Editar Hábito</h2>
          <p className="modal-subtitle-creation">Ajuste as informações do seu hábito.</p>
        </div>

        <div>
          <div className="form-group">
            <label>Nome do hábito</label>
            <input
              className="input-creation"
              name="nome"
              value={formData.nome}
              onChange={handleChange}
            />
          </div>

          <div className="form-group">
            <label>Descrição</label>
            <input
              className="input-creation"
              name="descricao"
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
                value={formData.categoria}
                onChange={handleChange}
              />
            </div>

            <div className="form-group">
              <label>Frequência</label>
              <input
                className="input-creation"
                name="frequencia"
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
          Salvar Alterações
        </button>

      </div>
    </div>
  );
};

export default EditarHabitoModal;