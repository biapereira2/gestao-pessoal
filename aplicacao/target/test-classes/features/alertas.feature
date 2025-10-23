# language: pt
Funcionalidade: Alertas Personalizados
  Como um usuário autenticado
  Quero criar e gerenciar alertas personalizados
  Para ser notificado sobre minhas metas semanais

  Cenário: Criar alerta personalizado com sucesso
    Dado que eu sou um usuário autenticado para alertas
    E eu possuo uma meta semanal cadastrada para alertas
    Quando eu crio um alerta personalizado para ser notificado quando faltar 2 dias para o fim da semana
    Então o alerta deve ser cadastrado com sucesso
    E o alerta deve aparecer na lista de alertas ativos

  Cenário: Criar alerta com dados inválidos
    Dado que eu sou um usuário autenticado para alertas
    E eu possuo uma meta semanal cadastrada para alertas
    Quando eu tento criar um alerta sem definir condição ou tempo
    Então eu devo receber um erro falando que "condição inválida"
    E o alerta não deve ser criado

  Cenário: Editar alerta existente
    Dado que eu sou um usuário autenticado para alertas
    E eu possuo uma meta semanal cadastrada para alertas
    E eu criei um alerta para ser notificado 3 dias antes do fim da semana
    Quando eu altero o alerta para ser notificado 1 dia antes do fim da semana
    Então o alerta deve ser atualizado corretamente
    E a lista de alertas deve refletir a alteração

  Cenário: Excluir alerta existente
    Dado que eu sou um usuário autenticado para alertas
    E eu possuo uma meta semanal cadastrada para alertas
    E eu criei um alerta para ser notificado 3 dias antes do fim da semana
    Quando eu excluo o alerta
    Então ele deve ser removido da lista de alertas
    E o alerta não deve mais aparecer na tela

  Cenário: Disparo de alerta baseado em condição
    Dado que eu sou um usuário autenticado para alertas
    E eu possuo uma meta semanal de 5 hábitos específica para alertas
    E eu criei um alerta para ser notificado quando faltar 2 dias para o fim da semana
    Quando chegar 2 dias antes do final da semana
    Então eu devo receber a notificação do alerta configurado
    E o alerta deve ser marcado como disparado no sistema
