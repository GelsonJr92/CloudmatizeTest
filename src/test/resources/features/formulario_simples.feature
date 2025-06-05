# language: pt
Funcionalidade: Automacao do Formulario Simples
  Como um usuario do Selenium Playground
  Eu quero testar a funcionalidade de formulario simples
  Para garantir que os campos de entrada funcionam corretamente

  Contexto:
    Dado que estou na pagina inicial do Selenium Playground

  @formulario @positivo
  Cenario: Enviar mensagem simples com sucesso
    Quando eu navego para a pagina "Simple Form Demo"
    E eu preencho o campo de mensagem com "Ola, este e um teste automatizado!"
    E eu clico no botao do formulario "Get Checked Value"
    Entao eu devo ver a mensagem "Ola, este e um teste automatizado!" exibida
 
  @formulario @positivo  
  Cenario: Calcular soma de dois numeros
    Quando eu navego para a pagina "Simple Form Demo"
    E eu preencho o primeiro numero com "25"
    E eu preencho o segundo numero com "15"
    E eu clico no botao do formulario "Get Sum"
    Entao eu devo ver o resultado da soma como "40"

  @formulario @negativo
  Cenario: Tentar calcular soma com valores invalidos
    Quando eu navego para a pagina "Simple Form Demo"
    E eu preencho o primeiro numero com "abc"
    E eu preencho o segundo numero com "xyz"
    E eu clico no botao do formulario "Get Sum"
    Entao eu devo ver uma mensagem de erro ou resultado invalido

  @formulario @vazio
  Cenario: Enviar mensagem vazia
    Quando eu navego para a pagina "Simple Form Demo"
    E eu deixo o campo de mensagem vazio
    E eu clico no botao do formulario "Get Checked Value"
    Entao eu devo ver uma mensagem vazia ou comportamento padrao

  @formulario @positivo
  Esquema do Cenario: Calcular soma com diferentes valores
    Quando eu navego para a pagina "Simple Form Demo"
    E eu preencho o primeiro numero com "<numero1>"
    E eu preencho o segundo numero com "<numero2>"
    E eu clico no botao do formulario "Get Sum"
    Entao eu devo ver o resultado da soma como "<resultado>"

    Exemplos:
      | numero1 | numero2 | resultado |
      | 10      | 20      | 30        |
      | 5       | 5       | 10        |
      | 100     | 50      | 150       |
      | 0       | 1       | 1         |
