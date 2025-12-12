import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import DashboardLayout from '../components/DashboardLayout';
import HabitoCard from '../components/Habitos/HabitoCard';
// ... outros imports de Modais
import CriarHabitoModal from '../components/Habitos/CriarHabitoModal';
import ConfirmacaoModal from '../components/Social/ConfirmacaoModal';
import EditarHabitoModal from '../components/Habitos/EditarHabitoModal';
import DetalhesHabitoModal from '../components/Habitos/DetalhesHabitoModal';
import { habitoService } from '../services/habitoService';
import { toast } from 'react-toastify';
import '../css/habitos.css';

const Habitos = () => {
  const { id } = useParams(); // 'id' é o ID do usuário
  const [habitos, setHabitos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [busca, setBusca] = useState('');

  const [modalCriarAberto, setModalCriarAberto] = useState(false);
  const [modalExclusao, setModalExclusao] = useState({ show: false, habito: null });
  const [modalEditar, setModalEditar] = useState({ show: false, habito: null });
  const [modalDetalhes, setModalDetalhes] = useState({ show: false, habito: null });

  useEffect(() => {
    carregarHabitos();
  }, [id]);

  const carregarHabitos = async () => {
    try {
      setLoading(true);
      // ...
      const data = await habitoService.listarPorUsuario(id);
      setHabitos(data);
    } catch (error) {
      toast.error("Erro ao carregar hábitos");
    } finally {
      setLoading(false);
    }
  };

  // ... (suas funções handleSalvarHabito, handleAtualizarHabito, confirmarRemocao)

  // =======================================================
  // FUNÇÃO PARA LIDAR COM O CHECK-IN (NOVA)
  // =======================================================
  /**
   * Chamada pelo HabitoCard após marcar/desmarcar o check-in.
   * Pode ser usada para atualizar o placar de pontos do usuário, por exemplo.
   * @param {string} habitoId - ID do hábito alterado.
   * @param {boolean} isCheckedIn - true se marcou, false se desmarcou.
   */
  const handleCheckinConcluido = (habitoId, isCheckedIn) => {
      // Por exemplo, você pode recarregar o progresso do usuário aqui,
      // mas como o backend atualiza a pontuação automaticamente,
      // esta função serve principalmente para fins de log ou atualizações visuais em outros lugares.
      console.log(`[EVENTO] Hábito ${habitoId} foi ${isCheckedIn ? 'marcado' : 'desmarcado'}.`);

      // Se você tiver um componente que mostra os pontos do usuário,
      // você chamaria a função de atualização dele aqui.
      // Exemplo: atualizarPontosGlobais();
  };

  // ... (funções de filtro)

  const habitosFiltrados = habitos.filter(h =>
    h.nome.toLowerCase().includes(busca.toLowerCase())
  );

  return (
    <DashboardLayout>
      <div className="habitos-page">
        {/* ... (Seção de Título e Input/Botão Criar) */}
        <div style={{ marginBottom: '25px' }}>
          <h1>Meus Hábitos</h1>
          <p>Gerencie sua rotina e acompanhe seu progresso diário.</p>
        </div>

        <div className="habitos-header-row">
          <input
            className="search-input-habito"
            placeholder="Digite o título do hábito..."
            value={busca}
            onChange={e => setBusca(e.target.value)}
          />
          <button className="btn-primary-habito" onClick={() => setModalCriarAberto(true)}>
            + Criar novo hábito
          </button>
        </div>

        <div className="habitos-list">
          {!loading && habitosFiltrados.length === 0 && <p>Nenhum hábito encontrado.</p>}

          {habitosFiltrados.map(habito => (
            <HabitoCard
              key={habito.id}
              habito={habito}
              usuarioId={id} // ⬅️ NOVO: ID DO USUÁRIO OBRIGATÓRIO PARA O CHECK-IN
              onRemover={(h) => setModalExclusao({ show: true, habito: h })}
              onEditar={(h) => setModalEditar({ show: true, habito: h })}
              onVerDetalhes={(h) => setModalDetalhes({ show: true, habito: h })}
              onCheckinConcluido={handleCheckinConcluido} // ⬅️ NOVO: CALLBACK DE AÇÃO
            />
          ))}
        </div>

        {/* ... (Seção de Modais) */}
        {modalCriarAberto && (
          <CriarHabitoModal
            onClose={() => setModalCriarAberto(false)}
            onSalvar={handleSalvarHabito}
            usuarioId={id}
          />
        )}

        {modalEditar.show && (
          <EditarHabitoModal
            habito={modalEditar.habito}
            onClose={() => setModalEditar({ show: false, habito: null })}
            onSalvar={handleAtualizarHabito}
          />
        )}

        {modalDetalhes.show && (
          <DetalhesHabitoModal
            habito={modalDetalhes.habito}
            onClose={() => setModalDetalhes({ show: false, habito: null })}
          />
        )}

        <ConfirmacaoModal
          isOpen={modalExclusao.show}
          onClose={() => setModalExclusao({ show: false, habito: null })}
          onConfirm={confirmarRemocao}
          titulo="Excluir hábito?"
          mensagem={`Deseja realmente excluir "${modalExclusao.habito?.nome}"?`}
        />
      </div>
    </DashboardLayout>
  );
};

export default Habitos;