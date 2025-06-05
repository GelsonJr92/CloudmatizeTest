# language: pt
Funcionalidade: Automacao de Radio Buttons
  Como um usuario do Selenium Playground
  Eu quero testar a funcionalidade de radio buttons
  Para garantir que as selecoes funcionam corretamente

  Contexto:
    Dado que estou na pagina inicial do Selenium Playground

  @radiobutton @faixaEtaria
  Cenario: Selecionar genero e faixa etaria completa
    Quando eu navego para a pagina "Radio Buttons Demo"
    E seleciono o radio button feminino
    E seleciono a faixa etaria "5 - 15"
    E eu clico no botao de radio "Get values"
    Entao devo ver a mensagem completa "5 - 15"

  @radiobutton @diferentesCombinacoesFaixaEtaria
  Esquema do Cenario: Selecionar diferentes combinacoes de genero e idade
    Quando eu navego para a pagina "Radio Buttons Demo"
    E seleciono o radio button "<genero>"
    E seleciono a faixa etaria "<idade>"
    E eu clico no botao de radio "Get values"    
    Entao devo ver a mensagem completa "<idade>"

    Exemplos:
      | genero   | idade   | genero_esperado |
      | masculino| 0 - 5   | Male            |
      | feminino | 5 - 15  | Female          |
      | feminino | 15 - 50 | Female          |

  @radiobutton @comportamento
  Cenario: Verificar que apenas um genero pode ser selecionado
    Quando eu navego para a pagina "Radio Buttons Demo"
    E seleciono o radio button masculino
    E seleciono o radio button feminino
    Entao o radio button feminino deve estar selecionado
    E o radio button masculino deve estar desmarcado
