#language: pt
Funcionalidade: Gerenciamento de Rotinas
Como um usuário autenticado
Quero criar, atualizar, listar e excluir rotinas
Para organizar melhor meus hábitos e atividades diárias


Cenario: Criar uma rotina com sucesso
Dado que sou um usuário autenticado para rotinas
Quando eu tento criar uma rotina chamada "Rotina Matinal"
Entao a rotina "Rotina Matinal" deve estar na minha lista de rotinas

Cenario: Tentar criar uma rotina com o nome em branco
Dado que sou um usuário autenticado para rotinas
Quando eu tento criar uma rotina com o nome em branco
Entao eu devo receber um erro de rotina informando que "não pode ser vazio"


Cenario: Listar rotinas existentes do usuário
Dado que sou um usuário autenticado para rotinas
Dado que eu possuo as rotinas "Rotina A" e "Rotina B" cadastradas
Quando eu acesso minha lista de rotinas
Entao a lista de rotinas deve conter exatamente 2 rotinas
E a lista de rotinas deve incluir "Rotina A" e "Rotina B"


Cenario: Atualizar o nome de uma rotina existente
Dado que sou um usuário autenticado para rotinas
Dado que eu possuo uma rotina chamada "Rotina Antiga" com descrição "desc antiga"
Quando eu atualizo o nome da rotina "Rotina Antiga" para "Rotina Nova"
Entao a rotina "Rotina Nova" deve estar na minha lista de rotinas

Cenario: Tentar atualizar rotina com nome em branco
Dado que sou um usuário autenticado para rotinas
Dado que eu possuo uma rotina chamada "Rotina de Teste" com descrição "desc teste"
Quando eu tento atualizar o nome da rotina "Rotina de Teste" para um nome em branco
Entao eu devo receber um erro de rotina informando que "não pode ser vazio"


Cenario: Excluir uma rotina existente
Dado que sou um usuário autenticado para rotinas
Dado que eu possuo uma rotina chamada "Rotina Excluir" com descrição "desc excluir"
Quando eu excluo a rotina "Rotina Excluir"
Entao a rotina "Rotina Excluir" não deve mais existir

Cenario: Tentar excluir uma rotina inexistente
Dado que sou um usuário autenticado para rotinas
Quando eu tento excluir uma rotina inexistente com id "00000000-0000-0000-0000-000000000000"
Entao eu devo receber um erro de rotina informando que "não encontrada"