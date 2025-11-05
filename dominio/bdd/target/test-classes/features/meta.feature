Feature: Gerenciamento de Metas Semanais e Mensais
  Como usuário do sistema
  Eu quero gerenciar minhas metas de hábitos (criar, atualizar, excluir e receber alertas)
  Para poder acompanhar meus hábitos de forma organizada.

  # --- Criação de metas ---
  Scenario: Criar meta semanal com sucesso
    Given que eu sou um usuário autenticado para metas
    When eu crio uma meta "semanal" de 5 hábitos
    Then a meta deve estar cadastrada

  Scenario: Criar meta mensal com sucesso
    Given que eu sou um usuário autenticado para metas
    When eu crio uma meta "mensal" de 20 hábitos
    Then a meta deve estar cadastrada

  Scenario: Criar meta com tipo vazio
    Given que eu sou um usuário autenticado para metas
    When eu tento criar uma meta com tipo vazio
    Then eu devo receber um erro de meta informando que "tipo inválido"

  # --- Atualização de metas ---
  Scenario: Atualizar meta existente
    Given que eu sou um usuário autenticado para metas
    And eu já possuo uma meta semanal de 3 hábitos cadastrada
    When eu atualizo a quantidade da meta para 7
    Then a lista de metas deve conter 1 meta com a quantidade atualizada

  # --- Exclusão de metas ---
  Scenario: Excluir meta existente
    Given que eu sou um usuário autenticado para metas
    And eu já possuo uma meta semanal de 4 hábitos cadastrada
    When eu excluo a meta existente
    Then a lista de metas deve estar vazia

  # --- Alertas de proximidade de falha ---
  Scenario: Receber alerta de meta semanal quase não cumprida
    Given que eu sou um usuário autenticado para metas
    And eu possuo uma meta semanal de 5 hábitos
    And só completei 1 hábito até agora
    When estiver próximo do final da semana
    Then devo receber um alerta informando que estou perto de não cumprir a meta

  Scenario: Receber alerta de meta mensal quase não cumprida
    Given que eu sou um usuário autenticado para metas
    And eu possuo uma meta mensal de 20 hábitos
    And só completei 5 hábitos até agora
    When estiver próximo do final do mês
    Then devo receber um alerta informando que estou perto de não cumprir a meta
