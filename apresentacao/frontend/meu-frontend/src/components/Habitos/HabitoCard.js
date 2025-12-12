import React, { useState, useEffect } from 'react';
import { checkinService } from '../../services/checkinService'; // Importa o novo serviço
import { toast } from 'react-toastify'; // Assume que 'toast' foi importado e está disponível
import '../../css/habitos.css'; // Mantenha seu CSS

// Adicionamos 'usuarioId' e 'onCheckinConcluido' como novos props
const HabitoCard = ({ habito, onRemover, onEditar, onVerDetalhes, usuarioId, onCheckinConcluido }) => {
  const [loadingCheckin, setLoadingCheckin] = useState(false);
  const [fezCheckinHoje, setFezCheckinHoje] = useState(false);

  // Obtém a data de hoje no formato YYYY-MM-DD para comunicação com o backend
  const hoje = new Date().toISOString().split('T')[0];

  // Efeito para verificar o status do check-in de HOJE ao carregar o Card
  useEffect(() => {
    const verificarCheckin = async () => {
      if (!usuarioId || !habito.id) return;

      try {
        // Lista todas as datas de check-in para este hábito
        const historico = await checkinService.listarPorHabito(habito.id, usuarioId);
        // Verifica se a data de hoje está presente no histórico
        setFezCheckinHoje(historico.includes(hoje));
      } catch (error) {
        console.error("Erro ao verificar check-in:", error);
        // Em caso de erro, assumimos que não foi feito para não bloquear
        setFezCheckinHoje(false);
      }
    };

    verificarCheckin();
  }, [habito.id, usuarioId, hoje]);

  // Handler para Marcar ou Desmarcar o Check-in
  const handleToggleCheckin = async () => {
    setLoadingCheckin(true);
    try {
        if (fezCheckinHoje) {
            // Se já fez, vamos desmarcar
            await checkinService.desmarcar(habito.id, usuarioId, hoje);
            toast.info("Check-in desmarcado. Pontuação subtraída.");
            setFezCheckinHoje(false);
            // Notifica o componente pai (Habitos.js) sobre a mudança
            onCheckinConcluido(habito.id, false);
        } else {
            // Se não fez, vamos marcar
            await checkinService.marcar(habito.id, usuarioId, hoje);
            toast.success("Check-in realizado! Pontos adicionados.");
            setFezCheckinHoje(true);
            // Notifica o componente pai (Habitos.js) sobre a mudança
            onCheckinConcluido(habito.id, true);
        }
    } catch (error) {
        // Captura a mensagem de erro do backend (ex: erro de duplicidade)
        const msg = error.message || "Erro desconhecido na operação de check-in.";
        toast.error(msg);
    } finally {
        setLoadingCheckin(false);
    }
  };

  const categoriaDisplay = habito.categoria || 'Geral';

  return (
    <div className="habito-card">
      <div className="habito-info">
        <div className="habito-title">{habito.nome}</div>
        <div className="habito-meta">
           <span style={{fontWeight: 600}}>{categoriaDisplay.toUpperCase()}</span>
           <span className="meta-separator"></span>
           <span>{habito.frequencia}</span>
        </div>
      </div>

      <div className="habito-actions">
        <button
            className="btn-outline"
            onClick={() => onVerDetalhes(habito)}
        >
            Ver detalhes
        </button>

        <button
            className="btn-outline"
            onClick={() => onEditar(habito)}
        >
            Editar
        </button>

        {/* BOTÃO DE CHECK-IN DINÂMICO */}
        <button
          className="btn-outline"
          onClick={handleToggleCheckin}
          disabled={loadingCheckin} // Desabilita durante o processamento
          style={{
            // Estilo Verde Escuro/Texto Branco se o check-in foi feito
            backgroundColor: fezCheckinHoje ? '#2e7d32' : '#e8f5e9',
            borderColor: '#2e7d32', // Borda sempre verde
            color: fezCheckinHoje ? '#FFFFFF' : '#2e7d32', // Cor do texto
            fontWeight: 700,
            cursor: loadingCheckin ? 'not-allowed' : 'pointer'
          }}
        >
           {loadingCheckin
             ? 'Processando...'
             : (fezCheckinHoje ? '✅ Desmarcar' : 'Marcar como feito')}
        </button>

        <button className="btn-danger" onClick={() => onRemover(habito)}>
          Remover
        </button>
      </div>
    </div>
  );
};

export default HabitoCard;