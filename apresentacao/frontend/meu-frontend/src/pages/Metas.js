import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import DashboardLayout from '../components/DashboardLayout';
import MetaCard from '../components/Metas/MetaCard';
import CriarMetaModal from '../components/Metas/CriarMetaModal';
import ConfirmacaoModal from '../components/Social/ConfirmacaoModal';
import EditarMetaModal from '../components/Metas/EditarMetaModal';
import DetalhesMetaModal from '../components/Metas/DetalhesMetaModal';
import { metaService } from '../services/metaService';
import { toast } from 'react-toastify';
import '../css/habitos.css';

const calcularProgressoMedio = (metas) => {
  if (metas.length === 0) return 0;
  const totalProgresso = metas.reduce((sum, meta) => {
    const progresso = meta.quantidade > 0
      ? (meta.habitosCompletos / meta.quantidade) * 100
      : 0;
    return sum + progresso;
  }, 0);
  return Math.round(totalProgresso / metas.length);
};

const Metas = () => {
  const { id } = useParams();
  const [metas, setMetas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [busca, setBusca] = useState('');

  const [modalCriarAberto, setModalCriarAberto] = useState(false);
  const [modalExclusao, setModalExclusao] = useState({ show: false, meta: null });
  const [modalEditar, setModalEditar] = useState({ show: false, meta: null });
  const [modalDetalhes, setModalDetalhes] = useState({ show: false, meta: null });

  const [totalCompletas, setTotalCompletas] = useState(0);
  const [progressoMedio, setProgressoMedio] = useState(0);

  useEffect(() => {
    carregarMetas();
  }, [id]);

  const carregarMetas = async () => {
    if (!id) {
      setLoading(false);
      return;
    }
    try {
      setLoading(true);
      const data = await metaService.listarResumosExpandido(id);
      setMetas(data);

      const completas = data.filter(m => m.quantidade > 0 && (m.habitosCompletos / m.quantidade) === 1).length;
      setTotalCompletas(completas);
      setProgressoMedio(calcularProgressoMedio(data));
    } catch (error) {
      console.error("Erro ao carregar metas:", error);
      toast.error("Erro ao carregar metas. Verifique a conexão com a API.");
    } finally {
      setLoading(false);
    }
  };

  const handleSalvarMeta = async (dadosForm) => {
    try {
      const dadosAjustados = { ...dadosForm, prazo: null, alertaProximoFalha: false };
      await metaService.criar(dadosAjustados);
      toast.success("Meta criada com sucesso!");
      setModalCriarAberto(false);
      carregarMetas();
    } catch (error) {
      toast.error("Erro ao criar meta: " + error.message);
    }
  };

  const handleAtualizarMeta = async (metaId, dadosForm) => {
    try {
      const dadosAjustados = { ...dadosForm, prazo: null, alertaProximoFalha: false };
      await metaService.atualizar(metaId, dadosAjustados);
      toast.success("Meta atualizada!");
      setModalEditar({ show: false, meta: null });
      carregarMetas();
    } catch (error) {
      toast.error("Erro ao atualizar meta: " + error.message);
    }
  };

  const confirmarRemocao = async () => {
    if (!modalExclusao.meta) return;
    try {
      await metaService.remover(modalExclusao.meta.id);
      setMetas(metas.filter(m => m.id !== modalExclusao.meta.id));
      toast.info("Meta removida.");
      setModalExclusao({ show: false, meta: null });
    } catch (error) {
      toast.error("Erro ao remover meta.");
    }
  };

  const metasFiltradas = metas.filter(m =>
    m.descricao && m.descricao.toLowerCase().includes(busca.toLowerCase())
  );

  // ✅ Metas em andamento reais
  const metasEmAndamento = metas.filter(m => {
    const hoje = new Date();
    const prazo = m.prazo ? new Date(m.prazo) : null;
    const prazoTerminou = prazo && prazo < hoje;
    const progresso = m.quantidade > 0 ? (m.habitosCompletos / m.quantidade) * 100 : 0;
    return progresso < 100 && !prazoTerminou;
  });

  const totalMetas = metas.length;

  return (
    <DashboardLayout>
      <div className="habitos-page" style={{ padding: '0 20px' }}>

        {/* === HEADER/DASHBOARD DE METAS === */}
        <div style={{ marginBottom: '30px' }}>
          <h1 style={{ fontWeight: 800 }}>Gerenciador de Metas</h1>
          <p style={{ color: 'var(--text-secondary)' }}>Gerencie suas metas semanais e mensais de hábitos</p>

          <div style={{
              display: 'flex', gap: '20px', marginTop: '20px', flexWrap: 'wrap',
              borderBottom: '1px solid #E0E0E0', paddingBottom: '20px'
          }}>

            {/* Total de Metas */}
            <div className="habito-card" style={{ flex: 1, minWidth: '200px', padding: '15px' }}>
              <h3 style={{ fontSize: '16px', fontWeight: 700, color: 'var(--text-primary)', marginBottom: '5px' }}>Total de Metas</h3>
              <div style={{ fontSize: '28px', fontWeight: 800, color: 'var(--text-primary)' }}>
                  {totalMetas}
              </div>
              <p style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>
                  {totalCompletas} completadas
              </p>
            </div>

            {/* Progresso Médio */}
            <div className="habito-card" style={{ flex: 1, minWidth: '200px', padding: '15px' }}>
              <h3 style={{ fontSize: '16px', fontWeight: 700, color: 'var(--text-primary)', marginBottom: '5px' }}>Progresso Médio</h3>
              <div style={{ fontSize: '28px', fontWeight: 800, color: 'var(--text-primary)' }}>
                  {progressoMedio}%
              </div>
              <p style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>
                  Todas as metas
              </p>
            </div>

            {/* Metas em Andamento */}
            <div className="habito-card" style={{ flex: 1, minWidth: '200px', padding: '15px' }}>
              <h3 style={{ fontSize: '16px', fontWeight: 700, color: 'var(--text-primary)', marginBottom: '5px' }}>Metas em Andamento</h3>
              <div style={{ fontSize: '28px', fontWeight: 800, color: 'var(--text-primary)' }}>
                  {metasEmAndamento.length}
              </div>
              <p style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>
                  Foco atual
              </p>
            </div>

          </div>
        </div>

        {/* Minhas Metas (Lista e Filtro) */}
        <div style={{ marginBottom: '30px' }}>
          <h2 style={{ fontSize: '20px', fontWeight: 800, color: 'var(--text-primary)', marginBottom: '15px' }}>Minhas Metas</h2>

          <div className="habitos-header-row">
            <input
              className="search-input-habito"
              placeholder="Digite a descrição da meta..."
              value={busca}
              onChange={e => setBusca(e.target.value)}
            />
            <button className="btn-primary-habito" onClick={() => setModalCriarAberto(true)}>
              + Criar nova meta
            </button>
          </div>

          <div className="habitos-list">
            {loading && <p>Carregando metas...</p>}
            {!loading && metasFiltradas.length === 0 && <p>Nenhuma meta encontrada.</p>}

            {metasFiltradas.map(meta => (
              <MetaCard
                key={meta.id}
                meta={meta}
                onRemover={(m) => setModalExclusao({ show: true, meta: m })}
                onEditar={(m) => setModalEditar({ show: true, meta: m })}
                onVerDetalhes={(m) => setModalDetalhes({ show: true, meta: m })}
              />
            ))}
          </div>
        </div>

        {/* MODAIS */}
        {modalCriarAberto && (
          <CriarMetaModal
            onClose={() => setModalCriarAberto(false)}
            onSalvar={handleSalvarMeta}
            usuarioId={id}
          />
        )}

        {modalEditar.show && (
          <EditarMetaModal
            meta={modalEditar.meta}
            onClose={() => setModalEditar({ show: false, meta: null })}
            onSalvar={handleAtualizarMeta}
          />
        )}

        {modalDetalhes.show && (
          <DetalhesMetaModal
            meta={modalDetalhes.meta}
            onClose={() => setModalDetalhes({ show: false, meta: null })}
          />
        )}

        <ConfirmacaoModal
          isOpen={modalExclusao.show}
          onClose={() => setModalExclusao({ show: false, meta: null })}
          onConfirm={confirmarRemocao}
          titulo="Excluir Meta?"
          mensagem={`Deseja realmente excluir a meta "${modalExclusao.meta?.descricao || 'selecionada'}"?`}
        />
      </div>
    </DashboardLayout>
  );
};

export default Metas;
