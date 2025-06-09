
# 🚀 Cloudmatize Test Automation

Projeto de automação de testes E2E para o **Selenium Playground** utilizando **Selenium WebDriver**, **Cucumber BDD**, **Java 17** e **Maven**.

## 📋 Índice

- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Configuração do Ambiente](#-configuração-do-ambiente)
- [Executando os Testes](#-executando-os-testes)
- [Relatórios](#-relatórios)
- [Padrões Utilizados](#-padrões-utilizados)
- [Contribuindo](#-contribuindo)

## 🛠 Tecnologias Utilizadas

- **Java 17** - Linguagem de programação
- **Selenium WebDriver 4.15.0** - Automação de navegadores
- **Cucumber 7.14.0** - Framework BDD para escrita de cenários
- **JUnit 4.13.2** - Framework de testes unitários
- **Maven 3.x** - Gerenciamento de dependências e build
- **ExtentReports 5.0.9** - Geração de relatórios
- **Log4j 2.20.0** - Sistema de logs
- **Page Object Model** - Padrão de design para manutenibilidade

## 📁 Estrutura do Projeto

```
CloudmatizeTest/
├── src/
│   ├── main/java/br/com/cloudmatize/
│   │   ├── config/          # Gerenciamento de configurações
│   │   ├── driver/          # Factory do WebDriver
│   │   ├── enums/           # Enumerações (tipos de browser, etc.)
│   │   ├── pages/           # Page Objects (padrão Page Object Model)
│   │   └── utils/           # Utilitários e helpers
│   ├── test/java/br/com/cloudmatize/
│   │   ├── hooks/           # Hooks do Cucumber (Before/After)
│   │   ├── reports/         # Gerenciamento de relatórios
│   │   ├── runners/         # Test Runners do JUnit/Cucumber
│   │   ├── steps/           # Step Definitions do Cucumber
│   │   └── utils/           # Utilitários específicos de testes
│   ├── main/resources/
│   │   ├── config.properties      # Configurações da aplicação
│   │   ├── extent-config.xml      # Configurações do ExtentReports
│   │   ├── extent.properties      # Propriedades do ExtentReports
│   │   └── log4j2.xml            # Configurações de log
│   └── test/resources/
│       ├── features/              # Arquivos .feature do Cucumber
│       ├── extent-config.xml      # Config ExtentReports para testes
│       ├── extent.properties      # Propriedades ExtentReports para testes
│       └── log4j2.xml            # Configurações de log para testes
└── target/                        # Diretório de build do Maven
    ├── cucumber-reports/          # Relatórios do Cucumber
    ├── extent-reports/            # Relatórios do ExtentReports (index.html)
    └── logs/                      # Arquivos de log
```

## ⚙️ Configuração do Ambiente

### Pré-requisitos

1. **Java 17** instalado
2. **Maven 3.6+** instalado
3. **Git** instalado
4. **Navegadores**: Chrome, Firefox ou Edge

### Instalação

1. Clone o repositório:
```bash
git clone <url-do-repositorio>
cd CloudmatizeTest
```

2. Instale as dependências:
```bash
mvn clean install
```

3. Configure as propriedades em `src/main/resources/config.properties`:
```properties
base.url=https://www.lambdatest.com/selenium-playground/
browser.default=chrome
browser.headless=false
browser.maximize=true
implicit.wait=30
explicit.wait=15
page.load.timeout=60
```

## 🚀 Executando os Testes

### Execução Completa
```bash
mvn clean test
```

### Execução por Tags
```bash
# Executar apenas testes de formulário
mvn test -Dcucumber.filter.tags="@formulario"

# Executar apenas testes positivos
mvn test -Dcucumber.filter.tags="@positivo"

# Executar testes de dropdown positivos
mvn test -Dcucumber.filter.tags="@dropdown and @positivo"

# Excluir testes negativos
mvn test -Dcucumber.filter.tags="not @negativo"
```

### Execução com Diferentes Navegadores
```bash
# Chrome (padrão)
mvn test -Dbrowser.default=chrome

# Firefox
mvn test -Dbrowser.default=firefox

# Edge
mvn test -Dbrowser.default=edge

# Modo headless
mvn test -Dbrowser.headless=true
```

### Execução Paralela (se configurada)
```bash
mvn test -Dparallel=methods -DthreadCount=3
```


## 📊 Relatórios

### ExtentReports
- **Localização**: `target/extent-reports/index.html`
- **Características**: Relatório interativo com gráficos, screenshots e logs detalhados. Sempre sobrescrito a cada execução, garantindo limpeza automática.

### Cucumber Reports
- **HTML**: `target/cucumber-reports/html/`
- **JSON**: `target/cucumber-reports/Cucumber.json`
- **JUnit XML**: `target/cucumber-reports/Cucumber.xml`
- **Timeline**: `target/cucumber-reports/timeline/`

### Logs
- **Console**: Logs formatados no terminal
- **Arquivo**: `target/logs/automation.log`
## 🔄 Integração Contínua (CI/CD) e Fluxo Git

O projeto utiliza **GitHub Actions** para garantir qualidade e rastreabilidade:

- **Pipelines**: Execução automática de testes de regressão, smoke e validação de código a cada push ou Pull Request.
- **Upload de Relatórios**: Relatórios Extent e Cucumber são gerados e disponibilizados como artefatos do pipeline.
- **Branch principal**: O fluxo é padronizado para uso do branch `main`.
- **Pull Requests**: Mesmo em projetos solo, recomenda-se abrir PRs para manter histórico e rastreabilidade.

> Para detalhes, consulte os arquivos em `.github/workflows/`.

## ✨ Diferenciais do Projeto

- **Limpeza automática de relatórios** antes de cada execução (via task do VS Code)
- **Relatórios ExtentReports sempre atualizados** e sobrescritos (index.html)
- **Estrutura modular e enxuta** (DriverFactory centralizado, hooks organizados)
- **Padronização de branch e fluxo de PR**
- **Pipelines CI/CD robustos** com upload de artefatos
- **Logs detalhados e screenshots automáticos em falhas**

## 🏗 Padrões Utilizados

### Page Object Model (POM)
- Separação clara entre lógica de teste e interação com elementos
- Reutilização de código e facilidade de manutenção
- Localizadores centralizados em classes de página

### BDD com Cucumber
- Cenários escritos em Gherkin (português)
- Step Definitions organizadas por funcionalidade
- Reutilização de steps comuns

### Padrão Factory
- `DriverFactory` para gerenciamento centralizado do WebDriver
- `ConfigManager` para configurações com padrão Singleton

### Estrutura de Logs
- Logs estruturados com diferentes níveis (INFO, DEBUG, WARN, ERROR)
- Saída simultânea para console e arquivo

## 📝 Funcionalidades Cobertas

### Formulário Simples
- ✅ Envio de mensagens
- ✅ Cálculo de soma
- ✅ Validação de campos vazios
- ✅ Teste com valores inválidos

### Dropdown/Select Lists
- ✅ Seleção única em dropdown
- ✅ Multi-seleção de estados
- ✅ Validação de opções padrão
- ✅ Teste de diferentes combinações

### Radio Buttons
- ✅ Seleção de gênero
- ✅ Seleção de faixa etária
- ✅ Validação de seleção única
- ✅ Combinações diferentes de valores

## 🔧 Configurações Avançadas

### Personalização de Waits
```properties
# Espera implícita (segundos)
implicit.wait=30

# Espera explícita (segundos)
explicit.wait=15

# Timeout de carregamento de página (segundos)
page.load.timeout=60
```

### Configuração de Screenshots
- Screenshots automáticos em falhas
- Screenshots manuais disponíveis via método
- Armazenamento em `target/extent-reports/screenshots/`

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## 📞 Suporte

Para questões e suporte, entre em contato com a equipe de automação.

---



