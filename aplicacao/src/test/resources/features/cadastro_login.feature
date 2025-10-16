Feature: Cadastro e Login de Usuários
  Como um novo usuário
  Eu quero me cadastrar e fazer login
  Para poder acessar o meu sistema de gestão pessoal

  Scenario: Cadastro com sucesso
    Given que eu estou na página de cadastro
    When eu preencho o campo "Nome de Usuário" com "joaodasilva"
    And eu preencho o campo "Email" com "joao.silva@email.com"
    And eu preencho o campo "Senha" com "Senha@123"
    And eu preencho o campo "Confirmar Senha" com "Senha@123"
    And eu clico no botão "Cadastrar"
    Then o cadastro deve ser realizado com sucesso
    And o usuário "joao.silva@email.com" deve existir no sistema

  Scenario: Cadastro com email já registrado
    Given que o email "joao.silva@email.com" já está cadastrado no sistema
    When eu preencho o campo "Nome de Usuário" com "mariaaparecida"
    And eu preencho o campo "Email" com "joao.silva@email.com"
    And eu preencho o campo "Senha" com "Maria@456"
    And eu preencho o campo "Confirmar Senha" com "Maria@456"
    And eu clico no botão "Cadastrar"
    Then o sistema deve retornar a mensagem de erro "Já existe uma conta cadastrada com este email."

  Scenario: Senhas não coincidentes
    Given que eu estou na página de cadastro
    When eu preencho o campo "Nome de Usuário" com "pedrodiniz"
    And eu preencho o campo "Email" com "pedro.diniz@email.com"
    And eu preencho o campo "Senha" com "Senha@123"
    And eu preencho o campo "Confirmar Senha" com "Senha@456"
    And eu clico no botão "Cadastrar"
    Then o sistema deve retornar a mensagem de erro "As senhas não coincidem."