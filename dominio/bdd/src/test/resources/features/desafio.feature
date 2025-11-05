Feature: Compartilhamento de Hábitos e Desafios
  Como um usuário (criador ou convidado)
  Quero gerenciar desafios com amigos (criar, convidar, aceitar, sair)
  Para me motivar e acompanhar o progresso em grupo.

  @criacao
  Scenario: Criar um desafio com sucesso e enviar convites
    Given que o usuário "Criador" está autenticado
    And que os hábitos "Meditar" e "Beber Água" estão cadastrados para "Criador"
    And que o usuário "Convidado1" e "Convidado2" existem no sistema
    When o "Criador" inicia a criação de um novo desafio com o nome "Desafio da Saúde"
    And ele seleciona os hábitos "Meditar", "Beber Água"
    And ele convida os usuários "Convidado1" e "Convidado2"
    And ele finaliza a criação
    Then o desafio "Desafio da Saúde" deve ser criado com status "ATIVO"
    And o "Criador" deve ser listado como participante ativo
    And o "Convidado1" e o "Convidado2" devem ter recebido um convite pendente

  @convite_aceite
  Scenario: Convidado aceita o convite
    Given que o usuário "Criador" criou o desafio "Desafio de Leitura"
    And o usuário "Convidado1" possui um convite pendente para "Desafio de Leitura"
    When o "Convidado1" aceita o convite para o desafio "Desafio de Leitura"
    Then o "Convidado1" deve ser listado como participante ativo do desafio
    And a contagem de participantes deve aumentar
    And o convite para "Desafio de Leitura" deve ser resolvido

  @convite_rejeite
  Scenario: Convidado rejeita o convite
    Given que o usuário "Criador" criou o desafio "Desafio de Leitura"
    And o usuário "Convidado2" possui um convite pendente para "Desafio de Leitura"
    When o "Convidado2" rejeita o convite para o desafio "Desafio de Leitura"
    Then o "Convidado2" não deve ser listado como participante
    And a contagem de participantes não deve ser alterada
    And o convite para "Desafio de Leitura" deve ser resolvido

  @encerramento_criador
  Scenario: Criador encerra o desafio
    Given que o desafio "Desafio Rápido" está ativo com "Criador" e "Convidado1"
    And o "Criador" é o administrador do desafio
    When o "Criador" solicita o encerramento do desafio "Desafio Rápido"
    Then o desafio "Desafio Rápido" deve ser marcado como "ENCERRADO"
    And todos os participantes devem ser notificados sobre o fim

  @saida_participante
  Scenario: Participante sai de um desafio ativo
    Given que o desafio "Desafio de Grupo" está ativo com "Criador" e "Convidado1"
    When o "Convidado1" solicita a saída do desafio "Desafio de Grupo"
    Then o "Convidado1" não deve ser mais listado como participante
    And o desafio "Desafio de Grupo" deve permanecer ativo

  @progresso
  Scenario: Acompanhar progresso do grupo no desafio
    Given que o desafio "Desafio de Leitura" está ativo e inclui o hábito "Ler 30 minutos"
    And que "Criador" cumpriu o hábito por 3 dias
    And que "Convidado1" cumpriu o hábito por 5 dias
    When o "Criador" visualiza o ranking do desafio "Desafio de Leitura"
    Then ele deve ver o progresso total de 8 cumprimentos do hábito "Ler 30 minutos"
    And ele deve ver que "Convidado1" está na frente no ranking