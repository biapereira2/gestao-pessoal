# src/test/resources/features/alertas.feature
# language: pt
Funcionalidade: Alertas Personalizados
  Como um usuário autenticado
  Quero criar e gerenciar alertas personalizados
  Para ser notificado sobre minhas metas

  Cenário: Criar alerta personalizado com sucesso
    Dado que eu sou um usuário autenticado para alertas
    Quando eu crio um alerta com título "Alerta Semanal", descrição "Final da semana", data de disparo "2025-12-15" e categoria "Semanal"
    Então o alerta deve ser cadastrado com sucesso
    E o alerta deve aparecer na lista de alertas ativos

  Cenário: Criar alerta com dados inválidos
    Dado que eu sou um usuário autenticado para alertas
    Quando eu tento criar um alerta sem definir título ou data
    Então devo receber um erro falando que "Título obrigatório"
    E o alerta não deve ser criado

  Cenário: Editar alerta existente
    Dado que eu sou um usuário autenticado para alertas
    E eu criei um alerta com título "Alerta Semanal", descrição "Final da semana", data de disparo "2025-12-15" e categoria "Semanal"
    Quando eu edito o alerta para título "Alerta Diário", descrição "Fim do dia", data "2025-12-12" e categoria "Diário"
    Então o alerta deve ser atualizado corretamente
    E a lista de alertas do usuário deve refletir a alteração

  Cenário: Excluir alerta existente
    Dado que eu sou um usuário autenticado para alertas
    E eu criei um alerta com título "Alerta Semanal", descrição "Final da semana", data de disparo "2025-12-15" e categoria "Semanal"
    Quando eu excluo o alerta
    Então ele deve ser removido da lista de alertas
    E o alerta não deve mais aparecer na tela

  Cenário: Disparo de alerta
    Dado que eu sou um usuário autenticado para alertas
    E eu criei um alerta com título "Alerta Diário", descrição "Final do dia", data de disparo "2025-12-12" e categoria "Diário"
    Quando eu verifico se o alerta deve ser disparado
    Então o alerta deve ser marcado como disparado no sistema
