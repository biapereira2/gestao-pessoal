// src/components/Alertas/CriarAlertaModal.js
import React, { useState } from 'react';
import '../../css/alertas.css';

const CriarAlertaModal = ({ usuarioId, onClose, onSalvar, categoriasDisponiveis: categoriasIniciaisProp }) => {
  const [titulo, setTitulo] = useState('');
  const [descricao, setDescricao] = useState('');
  const [dataDisparo, setDataDisparo] = useState(''); // data armazenada como string yyyy-MM-dd

  const [categoriasDisponiveis, setCategoriasDisponiveis] = useState(categoriasIniciaisProp);
  const [categoria, setCategoria] = useState(categoriasIniciaisProp.includes('Geral') ? 'Geral' : categoriasIniciaisProp[0] || '');
  const [novaCategoriaInput, setNovaCategoriaInput] = useState('');
  const [mostraInputNovaCategoria, setMostraInputNovaCategoria] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!titulo) return alert("Preencha o Título");
    if (!dataDisparo) return alert("Preencha a Data de Disparo");

    onSalvar({
      usuarioId,
      titulo,
      descricao,
      dataDisparo, // envia como string yyyy-MM-dd
      categoria
    });
  };

  const handleAdicionarCategoria = () => {
    if (novaCategoriaInput.trim() === '') return alert("O nome da categoria não pode ser vazio.");

    const novaCat = novaCategoriaInput.trim();
    if (categoriasDisponiveis.map(c => c.toLowerCase()).includes(novaCat.toLowerCase())) {
      return alert("Esta categoria já existe.");
    }

    const novasCategorias = [...categoriasDisponiveis, novaCat];
    setCategoriasDisponiveis(novasCategorias);
    setCategoria(novaCat);
    setNovaCategoriaInput('');
    setMostraInputNovaCategoria(false);
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content-creation">
        <button className="btn-close-absolute" onClick={onClose}>&times;</button>
        <h2>Criar Alerta</h2>
        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>

          <label>Título (Obrigatório)</label>
          <input type="text" value={titulo} onChange={e => setTitulo(e.target.value)} />

          <label>Data de Disparo (Obrigatório)</label>
          <input type="date" value={dataDisparo} onChange={e => setDataDisparo(e.target.value)} />

          <label>Categoria</label>
          <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
            <select value={categoria} onChange={e => setCategoria(e.target.value)} style={{ flexGrow: 1 }}>
              {categoriasDisponiveis.map(cat => (
                <option key={cat} value={cat}>{cat}</option>
              ))}
            </select>
            <button
              type="button"
              className="btn-outline"
              onClick={() => setMostraInputNovaCategoria(true)}
              style={{ width: 'auto', flexShrink: 0 }}
            >
              + Nova
            </button>
          </div>

          {mostraInputNovaCategoria && (
            <div style={{ display: 'flex', gap: '10px', marginTop: '-5px' }}>
              <input
                type="text"
                placeholder="Nome da nova categoria"
                value={novaCategoriaInput}
                onChange={e => setNovaCategoriaInput(e.target.value)}
                style={{ flexGrow: 1 }}
              />
              <button
                type="button"
                className="btn-primary-alerta"
                onClick={handleAdicionarCategoria}
                style={{ width: 'auto', flexShrink: 0 }}
              >
                Adicionar
              </button>
              <button
                type="button"
                className="btn-danger"
                onClick={() => setMostraInputNovaCategoria(false)}
                style={{ width: 'auto', flexShrink: 0 }}
              >
                Cancelar
              </button>
            </div>
          )}

          <label>Descrição (Opcional)</label>
          <textarea value={descricao} onChange={e => setDescricao(e.target.value)} rows="3" />

          <button type="submit" className="btn-primary-alerta btn-block">Salvar</button>
        </form>
      </div>
    </div>
  );
};

export default CriarAlertaModal;
