# 🏆 Sistema de Gestão de Hábitos

O **Sistema de Gestão de Hábitos** é uma aplicação que auxilia o usuário a **criar, acompanhar e manter hábitos** em sua rotina, proporcionando evolução pessoal e engajamento através de gamificação e relatórios detalhados.

---

## 🎯 Objetivo
Ajudar o usuário a estabelecer hábitos consistentes, monitorar seu progresso e manter a motivação através de sequências (streaks), metas personalizadas, lembretes e interações sociais.

---

## ⚙ Funcionalidades

- **Criação e personalização de hábitos**  
  - Nome, descrição, categoria (saúde, estudo, finanças, lazer) e frequência (diária, semanal, mensal)
- **Registro de cumprimento**  
  - Calendário para marcar os dias de prática de cada hábito
- **Acompanhamento de progresso**  
  - Streaks (dias consecutivos), metas individuais, gráficos de evolução, médias e comparativos semanais/mensais
- **Gamificação**  
  - Pontos, medalhas e conquistas conforme o usuário cumpre hábitos
- **Notas pessoais**  
  - Adicionar aprendizados, sentimentos ou dificuldades relacionadas a cada hábito
- **Interação social**  
  - Conectar amigos, visualizar ranking de desempenho e compartilhar progresso
- **Notificações e lembretes**  
  - Alertas em horários configurados pelo usuário

---

## 📊 Dashboard e Estatísticas (versão simples)

- Visualizar **hábitos mais cumpridos**  
- Ver **streaks atuais**  
- Consultar **gráficos de progresso**  
- **Atualização automática de streaks** ao abrir o dashboard  
- Registro de **visitas ao dashboard** para métricas de engajamento

---

## 🛠 Tecnologias

- **Back-end:** Python (Django) ou Java (Spring Boot)  
- **Banco de Dados:** SQLite / PostgreSQL  
- **Testes:** Cucumber para BDD  
- **Frontend:** HTML, CSS, JavaScript (ou framework de sua escolha)  

---

## 📁 Estrutura do Projeto

- `models/` → Modelos de dados (Usuário, Hábito, Cumprimento, Streaks)  
- `views/` → Lógica de visualização e dashboard  
- `static/` → Arquivos de estilo e scripts  
- `templates/` → Protótipos de interface do usuário
