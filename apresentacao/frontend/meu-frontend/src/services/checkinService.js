const API_URL = "http://localhost:8080/checkins";

export const checkinService = {

    /**
     * Lista todos os check-ins (datas) para um hábito específico e usuário.
     * Endpoint: GET /checkins/habito/{habitoId}/usuario/{usuarioId}
     * @param {string} habitoId - O ID do hábito
     * @param {string} usuarioId - O ID do usuário
     * @returns {Promise<string[]>} Uma lista de datas (ex: ['2025-12-11', '2025-12-12'])
     */
    listarPorHabito: async (habitoId, usuarioId) => {
        try {
            const response = await fetch(`${API_URL}/habito/${habitoId}/usuario/${usuarioId}`);
            if (!response.ok) {
                // Se a API retornar um erro (ex: 404), tratamos como lista vazia,
                // mas lançamos erro se for outro problema de servidor.
                if (response.status === 404) return [];
                throw new Error(`Erro ${response.status}: ${await response.text()}`);
            }
            return await response.json();
        } catch (error) {
            console.error("Erro ao listar check-ins:", error);
            // Propagamos o erro para ser tratado no componente (toast)
            throw error;
        }
    },

    /**
     * Marca um check-in para um hábito na data especificada.
     * Endpoint: POST /checkins
     * @param {string} habitoId - O ID do hábito
     * @param {string} usuarioId - O ID do usuário
     * @param {string} data - A data do check-in (formato AAAA-MM-DD)
     */
    marcar: async (habitoId, usuarioId, data) => {
        const checkInForm = {
            habitoId: habitoId,
            usuarioId: usuarioId,
            data: data
        };

        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(checkInForm),
        });

        if (!response.ok) {
            // Se falhar (ex: 400 Bad Request por check-in duplicado), lança o erro do backend.
            throw new Error(await response.text() || "Erro ao marcar check-in");
        }
        // Retorna o objeto CheckIn criado
        return await response.json();
    },

    /**
     * Remove/Desmarca um check-in.
     * Endpoint: DELETE /checkins
     * ATENÇÃO: O `fetch` DELETE geralmente não aceita `body`.
     * Vamos usar uma abordagem que funcione com o Spring Boot que criamos,
     * passando os dados no corpo (se o backend aceitar) ou na URL.
     * Como o backend aceita body, usaremos a técnica do `DELETE` com `body`.
     * * @param {string} habitoId - O ID do hábito
     * @param {string} usuarioId - O ID do usuário
     * @param {string} data - A data do check-in (formato AAAA-MM-DD)
     */
    desmarcar: async (habitoId, usuarioId, data) => {
        const checkInForm = {
            habitoId: habitoId,
            usuarioId: usuarioId,
            data: data
        };

        // Nota: `fetch` DELETE não suporta body nativamente em todos os browsers/servidores.
        // Esta é uma implementação comum para APIs REST que exigem body no DELETE.
        const response = await fetch(API_URL, {
            method: "DELETE",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(checkInForm),
        });

        if (!response.ok) {
            throw new Error(await response.text() || "Erro ao desmarcar check-in");
        }
        return true;
    }
};