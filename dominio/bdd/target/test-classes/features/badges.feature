Feature: Recompensas e Conquistas (Badges)
  Como um usuário,
  Eu quero receber e visualizar badges
  Para ter um incentivo visual do meu progresso e engajamento.

  Scenario: 1. Ganhar badge ao atingir o nível requerido
    Given que sou um usuário autenticado
    And existem badges cadastradas no sistema
    And o usuário tem o nível atual 4 no ProgressoUsuario
    When o usuário atinge o nível 5 no ProgressoUsuario
    Then a badge "Nível 5" deve ser concedida ao usuário
    And a lista de conquistas do usuário deve conter 1 badge

  Scenario: 2. Ganhar badge ao atingir a quantidade de metas concluídas
    Given que sou um usuário autenticado
    And existem badges cadastradas no sistema
    And o usuário completou 2 metas com sucesso
    And existe a badge "Mestre de Metas" requerindo 3 metas concluídas
    When o usuário completa sua 3ª meta com sucesso
    Then a badge "Mestre de Metas" deve ser concedida ao usuário
    And a lista de conquistas do usuário deve conter a badge "Mestre de Metas"

  Scenario: 3. Listar badges conquistadas
    Given que sou um usuário autenticado
    And existem badges cadastradas no sistema
    And o usuário já conquistou as badges "Nível 5 e Nível 10"
    When eu acesso a lista de conquistas
    Then eu devo ver uma lista com 2 badges conquistadas

  Scenario: 4. Mostrar badges disponíveis (a desbloquear)
    Given que sou um usuário autenticado
    And existem badges cadastradas no sistema
    And o usuário já conquistou as badges "Nível 5"
    And existem as badges "Nível 10" e "Mestre de Metas" disponíveis
    When eu acesso a lista de badges disponíveis
    Then eu devo ver uma lista com 2 badges a desbloquear
    And a lista não deve conter a badge "Nível 5"