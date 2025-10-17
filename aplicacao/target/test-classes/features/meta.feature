Feature: Gerenciamento de Metas Semanais e Mensais
  Como um usuário do sistema,
  Eu quero gerenciar minhas metas (criar, atualizar, excluir e listar)
  Para poder acompanhar meus hábitos de forma organizada.

  Scenario: Criar meta semanal com sucesso
    Given que sou um usuário autenticado para metas
    When eu crio uma meta "semanal" de 5 hábitos
    Then a meta deve estar cadastrada

  Scenario: Criar meta com tipo vazio
    Given que sou um usuário autenticado para metas
    When eu tento criar uma meta com tipo vazio
    Then eu devo receber um erro de meta informando que "tipo inválido"

  Scenario: Atualizar meta existente
    Given que sou um usuário autenticado para metas
    And eu já possuo uma meta semanal de 3 hábitos cadastrada
    When eu atualizo a quantidade da meta para 7
    Then a lista de metas deve conter 1 meta

  Scenario: Excluir meta existente
    Given que sou um usuário autenticado para metas
    And eu já possuo uma meta semanal de 4 hábitos cadastrada
    When eu excluo a meta existente
    Then a lista de metas deve estar vazia
