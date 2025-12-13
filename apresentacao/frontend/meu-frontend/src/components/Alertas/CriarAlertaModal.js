// src/components/Alertas/CriarAlertaModal.js
import React, { useState } from 'react';
import '../../css/alertas.css';

const CriarAlertaModal = ({ usuarioId, onClose, onSalvar, categoriasDisponiveis }) => {
   const [titulo, setTitulo] = useState('');
   const [descricao, setDescricao] = useState('');
   const [dataDisparo, setDataDisparo] = useState('');

   // Garante que a categoria inicial seja válida
   const catInicial = categoriasDisponiveis.includes('Geral') ? 'Geral' : categoriasDisponiveis[0] || '';
   const [categoria, setCategoria] = useState(catInicial);
  
   const handleSubmit = (e) => {
      e.preventDefault();
      if (!titulo) return alert("Preencha o Título");
      if (!dataDisparo) return alert("Preencha a Data de Disparo");

      onSalvar({
         usuarioId,
         titulo,
         descricao,
         dataDisparo,
         categoria
      });
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
               </div>

               <label>Descrição (Opcional)</label>
               <textarea value={descricao} onChange={e => setDescricao(e.target.value)} rows="3" />

               <button type="submit" className="btn-primary-alerta btn-block">Salvar</button>
            </form>
         </div>
         </div>
   );
};

export default CriarAlertaModal;