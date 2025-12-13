// src/components/Alertas/GerenciarCategoriasModal.js
import React, { useState } from 'react';
import '../../css/alertas.css';

const GerenciarCategoriasModal = ({ categorias, onClose, onAdicionar, onRemover }) => {
    const [novaCategoriaInput, setNovaCategoriaInput] = useState('');
    const categoriasImutaveis = ["Geral", "Aniversário", "Férias", "Trabalho", "Saúde"]; // Categorias fixas

    const handleAdicionar = (e) => {
        e.preventDefault();
        const novaCat = novaCategoriaInput.trim();
        if (novaCat === '') return alert("O nome da categoria não pode ser vazio.");

        // A checagem de duplicidade é feita no componente pai (Alertas.js)
        onAdicionar(novaCat);
        setNovaCategoriaInput('');
    };

    // Filtra as categorias para mostrar apenas as que podem ser gerenciadas
    const categoriasExibidas = categorias.filter(cat => !cat.toLowerCase().includes('todos'));

    return (
        <div className="modal-overlay">
            <div className="modal-content-creation" style={{ maxWidth: '400px' }}>
                <button className="btn-close-absolute" onClick={onClose}>&times;</button>
                <h2>Gerenciar Categorias</h2>

                {/* Formulário de Adição */}
                <form onSubmit={handleAdicionar} style={{ display: 'flex', gap: '10px', marginBottom: '20px' }}>
                    <input
                        type="text"
                        placeholder="Nome da nova categoria"
                        value={novaCategoriaInput}
                        onChange={e => setNovaCategoriaInput(e.target.value)}
                        style={{ flexGrow: 1 }}
                    />
                    <button type="submit" className="btn-primary-alerta" style={{ width: 'auto' }}>
                        Adicionar
                    </button>
                </form>

                {/* Lista de Categorias */}
                <h3 style={{ borderBottom: '1px solid #eee', paddingBottom: '5px', marginBottom: '10px', fontSize: '16px' }}>Categorias Atuais ({categoriasExibidas.length})</h3>
                <div style={{ maxHeight: '300px', overflowY: 'auto' }}>
                    <ul style={{ listStyleType: 'none', padding: 0 }}>
                        {categoriasExibidas.map(cat => (
                            <li
                                key={cat}
                                style={{
                                    display: 'flex',
                                    justifyContent: 'space-between',
                                    alignItems: 'center',
                                    padding: '8px 0',
                                    borderBottom: '1px dotted #eee',
                                    fontSize: '14px'
                                }}
                            >
                                <span>{cat}</span>
                                {!categoriasImutaveis.includes(cat) && (
                                    <button
                                        type="button"
                                        className="btn-danger"
                                        onClick={() => onRemover(cat)}
                                        style={{ padding: '4px 8px', fontSize: '0.8em', width: 'auto' }}
                                    >
                                        Apagar
                                    </button>
                                )}
                                {categoriasImutaveis.includes(cat) && (
                                    <span style={{ color: '#aaa', fontSize: '0.8em' }}>Padrão</span>
                                )}
                            </li>
                        ))}
                    </ul>
                </div>

                <button onClick={onClose} className="btn-outline btn-block" style={{ marginTop: '20px' }}>
                    Fechar
                </button>
            </div>
        </div>
    );
};

export default GerenciarCategoriasModal;