// src/components/Alertas/EditarAlertaModal.js
import React, { useState, useEffect } from 'react';
import '../../css/alertas.css';

const EditarAlertaModal = ({ alerta, onClose, onSalvar, categoriasDisponiveis: categoriasIniciaisProp }) => {
     const [titulo, setTitulo] = useState('');
     const [descricao, setDescricao] = useState('');
     const [dataDisparo, setDataDisparo] = useState('');

     const [categoriasDisponiveis, setCategoriasDisponiveis] = useState(categoriasIniciaisProp);
     const [categoria, setCategoria] = useState('Geral');

     useEffect(() => {
          if (alerta) {
               setTitulo(alerta.titulo || '');
               setDescricao(alerta.descricao || '');
               setDataDisparo(alerta.dataDisparo || '');

               let categoriasAtualizadas = [...categoriasIniciaisProp];
               const catInicial = alerta.categoria || 'Geral';

               // Garante que a categoria atual do alerta está na lista para edição
               if (!categoriasIniciaisProp.includes(catInicial)) {
                    categoriasAtualizadas.push(catInicial);
               }

               setCategoriasDisponiveis(categoriasAtualizadas);
               setCategoria(catInicial);
          }
     }, [alerta, categoriasIniciaisProp]);

     const handleSubmit = (e) => {
          e.preventDefault();
          if (!titulo) return alert("Preencha o Título");
          if (!dataDisparo) return alert("Preencha a Data de Disparo");

          onSalvar(alerta.id, {
               titulo,
               descricao,
               dataDisparo, // envia string yyyy-MM-dd
               categoria
          });
     };

     return (
          <div className="modal-overlay">
               <div className="modal-content-creation">
                    <button className="btn-close-absolute" onClick={onClose}>&times;</button>
                    <h2>Editar Alerta</h2>
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

                         <button type="submit" className="btn-primary-alerta btn-block">Salvar Alterações</button>
                    </form>
               </div>
          </div>
     );
};

export default EditarAlertaModal;