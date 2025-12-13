import React from 'react';
import '../../css/alertas.css';
import format from 'date-fns/format';

// === FUNÇÃO AUXILIAR DE CONVERSÃO [YYYY, MM, DD] -> Date ===
const convertLocalData = (dataArray) => {
        if (!Array.isArray(dataArray) || dataArray.length < 3) return null;
        const [ano, mes, dia] = dataArray;
        return new Date(ano, mes - 1, dia); // JS: mês 0-indexado
};
// ===============================================================

const DetalhesAlertaModal = ({ alerta, onClose }) => {
    if (!alerta) return null;

    // Determina se o alerta já foi disparado
    let dataParaComparar;
    if (Array.isArray(alerta.dataDisparo)) {
            dataParaComparar = convertLocalData(alerta.dataDisparo);
    } else if (alerta.dataDisparo) {
            dataParaComparar = new Date(alerta.dataDisparo);
    }

    const isDisparado = alerta.disparado || (dataParaComparar && dataParaComparar < new Date());

    // Formata a data para exibição
    let dataFormatada = 'N/A';
    if (alerta.dataDisparo) {
            try {
                    let dataObj = Array.isArray(alerta.dataDisparo)
                            ? convertLocalData(alerta.dataDisparo)
                            : new Date(alerta.dataDisparo);
                    dataFormatada = format(dataObj, 'dd/MM/yyyy');
            } catch (error) {
                    console.error("Erro ao formatar a data de disparo:", error);
                    dataFormatada = alerta.dataDisparo; // fallback
            }
    }

    return (
        <div className="modal-overlay">
            <div className="modal-content-creation">
                <button className="btn-close-absolute" onClick={onClose}>&times;</button>
                <h2>Detalhes do Alerta</h2>

                <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', marginTop: '15px' }}>
                    <p><strong>Título:</strong> {alerta.titulo}</p>
                    <p><strong>Data de Disparo:</strong> {dataFormatada}</p>
                    <p><strong>Categoria:</strong> {alerta.categoria || 'N/A'}</p>

                    <p>
                        <strong>Descrição:</strong>
                        {alerta.descricao && alerta.descricao.trim() !== ""
                            ? alerta.descricao
                            : " Este alerta não possui descrição."}
                    </p>

                    <p><strong>Status:</strong> {isDisparado ? 'Disparado' : 'Ativo'}</p>
                </div>

                <button onClick={onClose} className="btn-primary-alerta btn-block" style={{ marginTop: '20px' }}>Fechar</button>
            </div>
        </div>
    );
};

export default DetalhesAlertaModal;