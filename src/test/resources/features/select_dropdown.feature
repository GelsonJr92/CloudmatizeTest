# language: pt
Funcionalidade: Automacao de Dropdown/Select Lists
  Como um usuario do Selenium Playground
  Eu quero testar a funcionalidade de listas suspensas
  Para garantir que as selecoes em dropdowns funcionam corretamente
  
  Contexto:
    Dado que estou na pagina inicial do Selenium Playground

@dropdown @positivo
Cenario: Selecionar estado no multi-select
    Quando eu navego para a pagina "Select Dropdown List"
    E eu seleciono "California" no multi-select de estados
    E eu clico no botao de dropdown "First Selected"
    Entao devo ver "California" no resultado do multi-select
  
@dropdownMultiplos @positivo
Cenario: Selecionar multiplos estados
    Quando eu navego para a pagina "Select Dropdown List"
    E eu seleciono "California" no multi-select de estados
    E eu seleciono "Texas" no multi-select de estados
    E eu clico no botao de dropdown "Get Last Selected"
    Entao o resultado deve conter "Texas"
  
@dropdown @borda
Cenario: Verificar dropdown sem selecao
    Quando eu navego para a pagina "Select Dropdown List"
    Entao a opcao "Please select" deve estar selecionada no dropdown

@dropdownMultiplosDias @positivo
Esquema do Cenario: Testar selecao de diferentes dias da semana
    Quando eu navego para a pagina "Select Dropdown List"
    E eu seleciono "<dia>" no dropdown de dias da semana
    Entao devo ver "Day selected :- <dia>" como resultado da selecao
    
    Exemplos:
      | dia       |
      | Sunday    |
      | Monday    |
      | Tuesday   |
      | Wednesday |
      | Thursday  |
      | Friday    |
      | Saturday  |

@dropdown @positivo
Esquema do Cenario: Testar selecao de diferentes estados
    Quando eu navego para a pagina "Select Dropdown List"
    E seleciono "<estado>" no multi-select de estados
    E eu clico no botao de dropdown "First Selected"
    Entao devo ver "<estado>" no resultado do multi-select
    
    Exemplos:
      | estado      |
      | California  |
      | Florida     |
      | New Jersey  |
      | Ohio        |
      | Texas       |
      | Pennsylvania|
      | Washington  |
