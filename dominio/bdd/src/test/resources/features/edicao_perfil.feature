Feature: Edição de Perfil do Usuário
  Como um usuário autenticado
  Quero editar meus dados
  Para manter meu perfil atualizado

  Scenario: Edição de perfil do usuário
    Given que o usuário "joaodasilva" está logado
    When ele navega para a página de "Editar Perfil"
    And ele preenche o campo "Nome de Usuário" com "joaodasilva_novo"
    And ele altera o campo "Email" para "novo.email.joao@email.com"
    And ele fornece sua "Senha Atual" corretamente
    And ele clica no botão "Salvar Alterações"
    Then ele deve ser redirecionado para a página de home
    And ele deve ver a mensagem de sucesso "Perfil atualizado com sucesso."

  Scenario: Falha ao alterar email ou senha sem fornecer a senha atual
    Given que o usuário "joaodasilva" está logado
    When ele navega para a página de "Editar Perfil"
    And ele tenta alterar o campo "Email" para "novo.email@email.com"
    And ele deixa o campo "Senha Atual" vazio
    And ele clica no botão "Salvar Alterações"
    Then ele deve permanecer na página de "Editar Perfil"
    And ele deve ver a mensagem de erro "A senha atual é obrigatória para alterar dados sensíveis."

  Scenario: Edição de senha com senhas novas não coincidentes
    Given que o usuário "joaodasilva" está logado
    When ele navega para a página de "Editar Perfil"
    And ele fornece sua "Senha Atual" corretamente
    And ele preenche o campo "Nova Senha" com "NovaSenha@123"
    And ele preenche o campo "Confirmar Nova Senha" com "NovaSenha@456"
    And ele clica no botão "Salvar Alterações"
    Then ele deve permanecer na página de "Editar Perfil"
    And ele deve ver a mensagem de erro "As novas senhas não coincidem."
