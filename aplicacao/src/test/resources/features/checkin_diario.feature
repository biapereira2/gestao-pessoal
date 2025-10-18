Feature: Check-in Diário de Hábitos
  Como um usuário,
  Eu quero rastrear meus hábitos diariamente
  Para que eu possa gerenciar meu progresso.

Scenario: 1. Marcar hábito como feito com sucesso (Dia Atual)
  Given que sou um usuário autenticado
  And eu já possuo um hábito cadastrado com o nome "Beber 2L de água"
  And que o dia atual é "01/11/2025"
  When eu clico no botão "Marcar como feito" para o hábito "Beber 2L de água" no dia "01/11/2025"
  Then o check-in deve ser registrado com sucesso para o hábito "Beber 2L de água" no dia "01/11/2025"

Scenario: 2. Desmarcar check-in (quando marcado errado)
  Given que sou um usuário autenticado
  And eu já possuo um hábito cadastrado com o nome "Beber 2L de água"
  And que o dia atual é "02/11/2025"
  And o hábito "Beber 2L de água" já está marcado como feito no dia "02/11/2025"
  When eu clico no botão "Desmarcar check-in" para o hábito "Beber 2L de água" no dia "02/11/2025"
  Then o check-in deve ser removido com sucesso para o hábito "Beber 2L de água" no dia "02/11/2025"
  And o hábito "Beber 2L de água" não deve ter check-in registrado no dia "02/11/2025"

Scenario: 3. Visualizar check-ins de um calendário (Mês)
  Given que sou um usuário autenticado
  And eu já possuo um hábito cadastrado com o nome "Academia"
  And que o dia atual é "10/11/2025"
  And o hábito "Academia" tem check-ins registrados nos dias "01/11/2025, 05/11/2025, 10/11/2025"
  When eu acesso a lista de check-ins para o hábito "Academia" no período de "01/11/2025" a "30/11/2025"
  Then eu devo ver uma lista com 3 check-ins

Scenario: 4. Impedir check-in duplicado (Regra de Negócio)
  Given que sou um usuário autenticado
  And eu já possuo um hábito cadastrado com o nome "Ler livro"
  And que o dia atual é "03/11/2025"
  And o hábito "Ler livro" já está marcado como feito no dia "03/11/2025"
  When eu clico no botão "Marcar como feito" para o hábito "Ler livro" no dia "03/11/2025"
  Then eu devo receber um erro de check-in duplicado

