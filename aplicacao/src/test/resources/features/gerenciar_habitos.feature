Feature: Gerenciamento de Hábitos de um Usuário
  Como um usuário do sistema,
  Eu quero gerenciar meus hábitos (criar, editar, excluir e listar)
  Para poder acompanhar e manter minhas rotinas de forma eficaz.

  Scenario: Criação de um novo hábito com sucesso
    Given que sou um usuário autenticado
    When eu tento criar um hábito chamado "Ler 10 páginas de um livro"
    Then o hábito "Ler 10 páginas de um livro" deve estar na minha lista de hábitos

  Scenario: Tentar criar um hábito com nome em branco
    Given que sou um usuário autenticado
    When eu tento criar um hábito com o nome em branco
    Then eu devo receber um erro informando que "O nome do hábito não pode ser vazio."
    And a minha lista de hábitos deve estar vazia

  Scenario: Tentar criar um hábito que já existe
    Given que sou um usuário autenticado
    And eu já possuo um hábito cadastrado com o nome "Beber 2 litros de água"
    When eu tento criar um hábito chamado "Beber 2 litros de água"
    Then eu devo receber um erro informando que "O hábito já existe"
    And a minha lista de hábitos deve conter apenas 1 hábito

  Scenario: Edição de um hábito existente com sucesso
    Given que sou um usuário autenticado
    And eu já possuo um hábito cadastrado com o nome "Fazer exercícios físicos"
    When eu atualizo o nome do hábito "Fazer exercícios físicos" para "Fazer 30 minutos de cardio"
    Then o hábito "Fazer 30 minutos de cardio" deve estar na minha lista de hábitos
    And o hábito "Fazer exercícios físicos" não deve mais existir

  Scenario: Tentar editar um hábito para um nome em branco
    Given que sou um usuário autenticado
    And eu já possuo um hábito cadastrado com o nome "Meditar por 15 minutos"
    When eu tento atualizar o nome do hábito "Meditar por 15 minutos" para um nome em branco
    Then eu devo receber um erro informando que "O nome do hábito não pode ser vazio."
    And o hábito "Meditar por 15 minutos" deve continuar na minha lista

  Scenario: Tentar editar um hábito inexistente
    Given que sou um usuário autenticado
    When eu tento atualizar o nome de um hábito inexistente com id "12345678-1234-1234-1234-1234567890ab" para "Qualquer Nome"
    Then eu devo receber um erro informando que "Hábito com ID 12345678-1234-1234-1234-1234567890ab não encontrado"

  Scenario: Exclusão de um hábito com sucesso
    Given que sou um usuário autenticado
    And eu já possuo um hábito cadastrado com o nome "Estudar inglês por 1 hora"
    When eu excluo o hábito "Estudar inglês por 1 hora"
    Then o hábito "Estudar inglês por 1 hora" não deve mais ser exibido na minha lista de hábitos

  Scenario: Tentar excluir um hábito inexistente
    Given que sou um usuário autenticado
    When eu tento excluir um hábito inexistente com id "12345678-1234-1234-1234-1234567890ab"
    Then eu devo receber um erro informando que "Hábito com ID 12345678-1234-1234-1234-1234567890ab não encontrado"

  Scenario: Listar múltiplos hábitos cadastrados
    Given que sou um usuário autenticado
    And eu já possuo os hábitos "Ler 10 páginas de um livro" e "Beber 2 litros de água" cadastrados
    When eu acesso a minha lista de hábitos
    Then a lista deve conter exatamente 2 hábitos
    And a lista deve incluir "Ler 10 páginas de um livro" e "Beber 2 litros de água"

  Scenario: Visualizar lista sem nenhum hábito cadastrado
    Given que sou um usuário autenticado
    When eu acesso a minha lista de hábitos
    Then a minha lista de hábitos deve estar vazia