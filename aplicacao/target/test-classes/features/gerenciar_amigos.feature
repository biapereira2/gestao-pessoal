Feature: Gerenciamento de Amizades
  Como um usuário do sistema,
  Eu quero poder adicionar, remover e listar amigos,
  Para criar uma rede social e me engajar com outros usuários.

  Scenario: Adicionar um amigo com sucesso
    Given que os usuários "Bia" com email "bia@email.com" e "Ana" com email "ana@email.com" estão cadastrados
    And eu estou logado como "bia@email.com"
    When eu adiciono o usuário com email "ana@email.com" como amigo
    Then o usuário "Ana" deve estar na minha lista de amigos
    And eu devo estar na lista de amigos de "Ana"

  Scenario: Remover um amigo com sucesso
    Given que os usuários "Bia" com email "bia@email.com" e "Ana" com email "ana@email.com" são amigos
    And eu estou logado como "bia@email.com"
    When eu removo o usuário com email "ana@email.com" dos meus amigos
    Then o usuário "Ana" não deve estar na minha lista de amigos
    And eu não devo estar na lista de amigos de "Ana"

  Scenario: Tentar adicionar a si mesmo como amigo
    Given que o usuário "Bia" com email "bia@email.com" está cadastrado
    And eu estou logado como "bia@email.com"
    When eu tento adicionar o usuário com email "bia@email.com" como amigo
    Then eu devo receber um erro de amizade informando "Não é possível adicionar a si mesmo como amigo."

  Scenario: Tentar adicionar um usuário que já é amigo
    Given que os usuários "Bia" com email "bia@email.com" e "Ana" com email "ana@email.com" são amigos
    And eu estou logado como "bia@email.com"
    When eu tento adicionar o usuário com email "ana@email.com" como amigo
    Then eu devo receber um erro de amizade informando "Este usuário já é seu amigo."

  Scenario: Tentar adicionar um usuário que não existe
    Given que o usuário "Bia" com email "bia@email.com" está cadastrado
    And eu estou logado como "bia@email.com"
    When eu tento adicionar o usuário com email "fantasma@email.com" como amigo
    Then eu devo receber um erro de amizade informando "Usuário com email fantasma@email.com não encontrado."

  Scenario: Listar amigos
    Given que os usuários "Bia" com email "bia@email.com", "Ana" com email "ana@email.com" e "Carlos" com email "carlos@email.com" estão cadastrados
    And eu estou logado como "bia@email.com"
    And eu adicionei "ana@email.com" e "carlos@email.com" como amigos
    When eu acesso minha lista de amigos
    Then minha lista de amigos deve conter 2 usuários
    And a lista deve incluir os nomes "Ana" e "Carlos"

  Scenario: Listar amigos quando não há nenhum
    Given que o usuário "Bia" com email "bia@email.com" está cadastrado
    And eu estou logado como "bia@email.com"
    When eu acesso minha lista de amigos
    Then minha lista de amigos deve estar vazia