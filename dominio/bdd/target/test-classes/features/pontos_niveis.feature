Feature: Sistema de Pontos e Níveis
  Como um usuário do sistema,
  Eu quero ganhar pontos e subir de nível ao cumprir meus hábitos
  Para me sentir motivado a manter minha rotina e acompanhar meu progresso.

  Scenario: Ganhar pontos ao realizar um check-in
    Given o hábito "Ler 10 páginas" está pendente no dia atual para o usuário
    And o hábito "Ler 10 páginas" vale 10 pontos
    And o usuário possui 90 pontos acumulados
    When o usuário realiza o check-in do hábito "Ler 10 páginas"
    Then o sistema deve registrar o cumprimento do hábito
    And adicionar 10 pontos ao total do usuário
    And exibir o novo total de 100 pontos ao usuário

  Scenario: Retirar pontos ao desfazer um check-in
    Given que o usuário possui 100 pontos acumulados
    And realizou o check-in do hábito "Meditar" hoje
    And o hábito "Meditar" vale 10 pontos
    When o usuário desfaz o check-in de "Meditar"
    Then o sistema deve remover 10 pontos do saldo do usuário
    And atualizar o total de pontos exibido ao usuário para 90 pontos

  Scenario: Subir de nível ao atingir meta de pontos
    Given que o usuário está no nível 1 com 90 pontos
    And o limite mínimo de pontos para o próximo nível é de 100 pontos
    And o usuário realiza um novo check-in que vale 10 pontos
    When o sistema adiciona os 10 pontos
    Then o saldo de pontos do usuário passa a ser 100
    And o sistema deve promover o usuário para o nível 2
    And aumentar o limite do próximo nível para 300
    And aumentar o limite mínimo do nível atual para 100
    And exibir uma mensagem de conquista

  Scenario: Cair de nível ao retirar pontos necessário para a meta
    Given que o usuário está no nível 2 com 100 pontos
    And o limite mínimo de pontos para o nível atual é de 100 pontos
    And o usuário desmarca um check-in que vale 10 pontos
    When o sistema retira os 10 pontos
    Then o saldo de pontos do usuário passa a ser 90
    And o sistema deve demover o usuário para o nível 1
    And setar o limite do próximo nível para 100
    And setar o limite mínimo do nível atual para 0

  Scenario: Visualizar pontos e nível atual
    Given que o usuário possui um total de 640 pontos e está no nível 4
    And o limite mínimo de pontos para o nível 5 é 1000
    When o usuário acessa seu perfil ou painel de progresso
    Then o sistema deve exibir 640 pontos no saldo do usuário
    And nível 4 como o nível do usuário
    And 360 pontos faltantes na barra de progresso de nível
