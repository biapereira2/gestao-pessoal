import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashboardLayout";
import CheckinCalendar from "../components/Checkins/CheckinCalendar"; // ⬅️ NOVO IMPORT
import { habitoService } from "../services/habitoService";
import { checkinService } from "../services/checkinService";
import { toast } from 'react-toastify';
import moment from 'moment';
import '../css/habitos.css';

const Checkins = () => {
  const { id } = useParams(); // ID do usuário
  const navigate = useNavigate();

  // Estado para controlar a visualização: 'list' (Histórico) ou 'calendar' (Calendário)
  const [viewMode, setViewMode] = useState('calendar');

  const [historico, setHistorico] = useState([]);
  const [loading, setLoading] = useState(true);
  const [pontuacao, setPontuacao] = useState(0);
  const [habitoNames, setHabitoNames] = useState({});

  useEffect(() => {
    // Carrega o histórico apenas se a visualização for 'list'
    if (viewMode === 'list') {
        carregarHistorico();
    } else {
        // Define loading como falso se estiver no calendário (a lógica de loading é interna ao CheckinCalendar)
        setLoading(false);
    }
  }, [id, viewMode]);

  const carregarHistorico = async () => {
    // ... (A lógica de carregamento do histórico é a mesma de antes,
    //      mantida dentro desta função para a visualização em lista)
    if (!id) return;
    setLoading(true);

    try {
        const habitosDoUsuario = await habitoService.listarPorUsuario(id);
        const nomesMap = {};
        let historicoAgregado = [];

        for (const habito of habitosDoUsuario) {
            nomesMap[habito.id] = habito.nome;
            const checkins = await checkinService.listarPorHabito(habito.id, id);

            checkins.forEach(data => {
                historicoAgregado.push({
                    habitoId: habito.id,
                    habitoNome: habito.nome,
                    data: data // AAAA-MM-DD
                });
            });
        }

        historicoAgregado.sort((a, b) => new Date(b.data) - new Date(a.data));

        setHabitoNames(nomesMap);
        setHistorico(historicoAgregado);

    } catch (error) {
        toast.error("Erro ao carregar o histórico de check-ins.");
    } finally {
        setLoading(false);
    }
  };

  const formatarData = (dataStr) => {
    return moment(dataStr).format('DD [de] MMMM [de] YYYY');
  };

  // Botão de Abas customizado para a página
  const TabButton = ({ mode, children }) => (
    <button
        onClick={() => setViewMode(mode)}
        style={{
            padding: '10px 15px',
            border: 'none',
            borderBottom: viewMode === mode ? '2px solid var(--accent-color)' : '2px solid transparent',
            backgroundColor: 'transparent',
            fontWeight: viewMode === mode ? '700' : '500',
            color: viewMode === mode ? 'var(--accent-color)' : 'var(--text-secondary)',
            cursor: 'pointer',
            transition: 'all 0.3s'
        }}
    >
        {children}
    </button>
  );

  return (
    <DashboardLayout>
      <div className="habitos-page">
        <div style={{ marginBottom: '25px' }}>
          <h1>Histórico de Check-ins e Progresso</h1>
          <p>Acompanhe suas conquistas diárias e pontuação acumulada.</p>
        </div>

        {/* Card de Pontuação (Mantido) */}
        <div className="habito-card" style={{padding: '15px 20px', marginBottom: '30px'}}>
            <h3 style={{color: 'var(--text-secondary)', fontSize: '14px', marginBottom: '5px'}}>
                Pontuação Total
            </h3>
            <div style={{fontSize: '32px', fontWeight: '800', color: 'var(--accent-color)'}}>
                {loading && viewMode === 'list' ? '...' : pontuacao} XP
            </div>
        </div>

        {/* SELEÇÃO DE ABAS */}
        <div style={{marginBottom: '20px', borderBottom: '1px solid #eee'}}>
            <TabButton mode="calendar">📅 Visualização Mensal</TabButton>
            <TabButton mode="list">📜 Histórico em Lista</TabButton>
        </div>

        {/* RENDERIZAÇÃO CONDICIONAL */}
        {viewMode === 'calendar' && (
            <CheckinCalendar usuarioId={id} />
        )}

        {viewMode === 'list' && (
            <div className="historico-list">
                <h3>Check-ins Recentes ({historico.length})</h3>
                {loading ? (
                    <p>Carregando histórico em lista...</p>
                ) : historico.length === 0 ? (
                    <p>Nenhum check-in registrado ainda.</p>
                ) : (
                    <div className="habitos-list" style={{marginTop: '10px'}}>
                        {historico.map((item, index) => (
                            <div key={index} className="habito-card" style={{
                                padding: '15px 20px',
                                display: 'flex',
                                justifyContent: 'space-between',
                                alignItems: 'center'
                            }}>
                                <div style={{display: 'flex', flexDirection: 'column'}}>
                                    <div style={{fontWeight: 700, fontSize: '16px'}}>
                                        ✅ {item.habitoNome}
                                    </div>
                                    <span style={{fontSize: '12px', color: 'var(--text-secondary)'}}>
                                        Hábito concluído
                                    </span>
                                </div>
                                <div style={{textAlign: 'right'}}>
                                    <div style={{fontWeight: 600, color: 'var(--text-primary)'}}>
                                        {formatarData(item.data)}
                                    </div>
                                    <span style={{fontSize: '12px', color: '#2E7D32'}}>
                                        +50 XP
                                    </span>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        )}

        <button className="btn-outline" onClick={() => navigate(`/dashboard/${id}`)} style={{marginTop: '30px'}}>
            ← Voltar para o Dashboard
        </button>
      </div>
    </DashboardLayout>
  );
};

export default Checkins;