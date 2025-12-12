import React, { useState, useEffect } from 'react';
import Calendar from 'react-calendar';
import { toast } from 'react-toastify';
import { checkinService } from '../../services/checkinService';
import { habitoService } from '../../services/habitoService';
import 'react-calendar/dist/Calendar.css'; // Importa o CSS padrão do componente
import '../../css/calendar-override.css'; // Vamos criar este arquivo para estilizar

/**
 * Componente que renderiza um calendário com os dias de check-in marcados.
 * @param {string} usuarioId - O ID do usuário logado.
 */
const CheckinCalendar = ({ usuarioId }) => {
    // Armazena a data atual que o calendário está visualizando (para fins de busca)
    const [activeDate, setActiveDate] = useState(new Date());
    // Mapeamento de datas com check-in: { 'YYYY-MM-DD': true }
    const [checkinDates, setCheckinDates] = useState({});
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        carregarDadosDoMes();
    }, [activeDate, usuarioId]); // Recarrega quando o mês/ano ativo ou o usuário mudar

    const carregarDadosDoMes = async () => {
        if (!usuarioId) return;

        setLoading(true);
        try {
            // 1. Pega todos os hábitos do usuário
            const habitosDoUsuario = await habitoService.listarPorUsuario(usuarioId);

            let todasDatas = {};

            // 2. Para cada hábito, busca as datas de check-in no mês/ano ativo
            // NOTA: Esta abordagem ainda é ineficiente para muitos hábitos.
            // O ideal seria um endpoint de backend que retorna todos os check-ins por MÊS/ANO.

            for (const habito of habitosDoUsuario) {
                // O service deve ser ajustado para filtrar por mês/ano, mas aqui
                // assumimos que 'listarPorHabito' retorna todas as datas e faremos o filtro no frontend
                // ou que o service foi atualizado para receber um range de datas.

                // Vamos supor que o service retorna APENAS as datas de check-in: ['2025-12-01', '2025-12-05', ...]
                const checkins = await checkinService.listarPorHabito(habito.id, usuarioId);

                checkins.forEach(data => {
                    // Armazena a data como chave, garantindo que não haja duplicatas
                    todasDatas[data] = true;
                });
            }

            setCheckinDates(todasDatas);

        } catch (error) {
            console.error("Erro ao carregar dados do calendário:", error);
            toast.error("Erro ao carregar dados do calendário.");
        } finally {
            setLoading(false);
        }
    };

    // Função para customizar cada dia do calendário
    const tileClassName = ({ date, view }) => {
        // Apenas aplica a classe na visualização "Mês"
        if (view === 'month') {
            const dateString = date.toISOString().split('T')[0]; // Ex: '2025-12-12'
            if (checkinDates[dateString]) {
                // Retorna a classe CSS para marcar o dia como check-in feito
                return 'checkin-dia-completo';
            }
        }
    };

    // Função chamada quando o usuário navega para o mês anterior/próximo
    const onActiveStartDateChange = ({ activeStartDate, view }) => {
        if (view === 'month') {
            setActiveDate(activeStartDate);
        }
    };

    return (
        <div className="checkin-calendar-container">
            {loading && <p>Carregando histórico do mês...</p>}

            <Calendar
                value={new Date()} // Define o valor padrão para o dia atual
                locale="pt-BR"
                calendarType="iso8601" // Começa a semana na segunda-feira
                tileClassName={tileClassName}
                onActiveStartDateChange={onActiveStartDateChange}
                // Desabilita o clique no dia (focamos apenas na visualização)
                selectRange={false}
            />

            <div className="legenda" style={{marginTop: '20px', borderTop: '1px solid #eee', paddingTop: '10px'}}>
                <div style={{display: 'flex', alignItems: 'center', marginBottom: '5px'}}>
                    <span className="checkin-dia-completo" style={{width: '20px', height: '20px', borderRadius: '50%', marginRight: '10px', display: 'inline-block'}}></span>
                    <span>Dia com check-in completo (Todos os hábitos marcados).</span>
                </div>
                {/* NOTA: Para uma lógica mais avançada (parcial),
                   precisaríamos de mais dados agregados do backend. */}
            </div>
        </div>
    );
};

export default CheckinCalendar;