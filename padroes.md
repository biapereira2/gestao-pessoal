# Padrões de Projeto Utilizados

## Observer

### Classes modificadas:
- Alerta

### Classes criadas:
- AlertaObserver
- AlertaService
  
### Funcionalidade de gerenciar rede de amigos:
- A implementação da funcionalidade social adota o padrão de projeto Observer para promover o desacoplamento entre o domínio de engajamento (amigos) e o sistema de gamificação (badges). O objetivo  é garantir que o serviço responsável por gerenciar amizades execute sua função primária sem criar dependências diretas ou rígidas com as regras de concessão de recompensas, respeitando o Princípio da Responsabilidade Única. O fluxo inicia no AmizadeService (publicador), que, imediatamente após salvar a nova relação no banco de dados, utiliza o ApplicationEventPublisher para disparar um AmizadeCriadaEvent. Na camada de reação, o componente BadgeListener atua como o observador, mapeado com a anotação @EventListener.
---

## Strategy

### Classes modificadas:
- Meta

### Classes criadas:
- EstrategiaAlertaMeta
- EstrategiaAlertaMetaDiaria
- EstrategiaAlertaMetaSemanal
- EstrategiaAlertaMetaMensal

---

## Template Method

### Classes modificadas:

### Classes criadas:

--- 

## Decorator

### Funcionalidade de gestão de hábitos:
- A implementação do módulo de gestão de hábitos adota o padrão de projeto Decorator para viabilizar a extensão dinâmica de funcionalidades, permitindo atribuir novas responsabilidades aos objetos em tempo de execução sem alterar sua estrutura original. A arquitetura define a interface HabitoBase como o componente fundamental, estabelecendo o contrato comum para todos os hábitos. A classe abstrata HabitoDecorator atua como o wrapper, mantendo uma referência ao objeto original e delegando a execução dos métodos, o que garante transparência na manipulação das instâncias. Dessa forma, funcionalidades adicionais são acopladas modularmente: o HabitoComPontuacaoExtra intercepta o cálculo de pontos para adicionar bônus ao resultado base, enquanto o HabitoComRestricaoHorario introduz validações temporais inéditas (podeRealizarAgora).

### Classes modificadas:

### Classes criadas:
