package br.com.cloudmatize.pages;

import br.com.cloudmatize.config.ConfigManager;
import br.com.cloudmatize.utils.WebElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object para a página Simple Form Demo
 * Contém métodos para interagir com formulários simples e operações de soma
 */
public class SimpleFormPage extends BasePage {
    private static final String SIMPLE_FORM_URL_PART = "simple-form-demo";
    private final ConfigManager config = ConfigManager.getInstance();    // Localizadores para formulário de mensagem única
    private final By messageInput = By.id("user-message");
    private final By showMessageButton = By.id("showInput"); // Correto: "Get Checked Value"
    private final By messageResult = By.id("message"); // Precisa ser descoberto onde aparece o resultado
    
    // Localizadores para formulário de soma
    private final By firstNumberInput = By.id("sum1");
    private final By secondNumberInput = By.id("sum2");
    // Removido getSumButton - usando método genérico por texto
    private final By sumResult = By.id("addmessage"); // Precisa ser descoberto onde aparece o resultado
    
    // Localizadores alternativos para resultados (caso os IDs acima estejam incorretos)
    private final By anyResultElement = By.xpath("//*[@id='message' or @id='addmessage' or contains(@class, 'result') or contains(@class, 'output')]");

    public SimpleFormPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navega para a página do formulário simples
     */
    public void navegarParaPaginaFormularioSimples() {
        try {
            String url = config.getBaseUrl() + SIMPLE_FORM_URL_PART;
            driver.get(url);
            logger.info("Navegando para página do formulário simples: {}", url);
            
            if (!validarPaginaCarregada()) {
                throw new RuntimeException("Página do formulário simples não carregou corretamente");
            }
            
            logger.info("Página do formulário simples carregada com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao navegar para página do formulário simples: {}", e.getMessage(), e);
            throw new RuntimeException("Falha na navegação para página do formulário simples", e);
        }
    }

    // =============================================================================
    // MÉTODOS PARA FORMULÁRIO DE MENSAGEM ÚNICA
    // =============================================================================

    /**
     * Preenche o campo de mensagem única
     */
    public void preencherMensagem(String mensagem) {
        try {
            logger.info("Preenchendo campo de mensagem única: '{}'", mensagem);
            preencherCampoSeguro(messageInput, mensagem);
        } catch (Exception e) {
            logger.error("Erro ao preencher mensagem: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao preencher campo de mensagem", e);
        }
    }    /**
     * Método genérico para clicar em qualquer botão pelo texto
     * @param textoBotao O texto exato do botão a ser clicado
     */
    public void clicarEmBotaoPorTexto(String textoBotao) {
        try {
            logger.info("Clicando no botão com texto: '{}'", textoBotao);
            WebElement botao = WebElementUtils.encontrarBotaoPorTexto(driver, textoBotao);
            botao.click();
            logger.info("Botão '{}' clicado com sucesso", textoBotao);
        } catch (Exception e) {
            logger.error("Erro ao clicar no botão '{}': {}", textoBotao, e.getMessage(), e);
            throw new RuntimeException("Falha ao clicar no botão: " + textoBotao, e);
        }
    }/**
     * Obtém o resultado da mensagem exibida
     * Procura em diferentes locais possíveis onde o resultado pode aparecer
     */
    public String obterResultadoMensagem() {
        try {
            // Lista de seletores possíveis para o resultado
            By[] possiveisResultados = {
                By.id("message"),
                By.id("display"),
                By.id("user_message"),
                By.xpath("//div[@id='message']"),
                By.xpath("//span[contains(@class, 'result')]"),
                By.xpath("//div[contains(@class, 'result')]"),
                By.xpath("//p[contains(@class, 'result')]"),
                By.xpath("//*[contains(text(), 'Ola, este e um teste automatizado!')]"),
                By.xpath("//div[following-sibling::*[contains(@id, 'user-message')]]"),
                anyResultElement
            };
            
            // Tenta encontrar o resultado em qualquer um dos locais
            for (By seletor : possiveisResultados) {
                try {
                    if (elementoEstaPresente(seletor)) {
                        String resultado = obterTextoSeguro(seletor);
                        if (resultado != null && !resultado.trim().isEmpty()) {
                            logger.info("Resultado da mensagem encontrado com seletor {}: '{}'", seletor, resultado);
                            return resultado;
                        }
                    }
                } catch (Exception e) {
                    logger.debug("Erro ao tentar seletor {}: {}", seletor, e.getMessage());
                }
            }
            
            logger.warn("Resultado da mensagem não encontrado em nenhum local conhecido");
            return "";
            
        } catch (Exception e) {
            logger.error("Erro ao obter resultado da mensagem: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Deixa o campo de mensagem vazio
     */
    public void deixarCampoMensagemVazio() {
        try {
            WebElementUtils.buscarElementoVisivel(driver, messageInput).clear();
            logger.info("Campo de mensagem deixado vazio");
        } catch (Exception e) {
            logger.error("Erro ao limpar campo de mensagem: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao limpar campo de mensagem", e);
        }
    }

    // =============================================================================
    // MÉTODOS PARA FORMULÁRIO DE SOMA
    // =============================================================================

    /**
     * Preenche o primeiro número para soma
     */
    public void preencherPrimeiroNumero(String numero) {
        try {
            logger.info("Preenchendo primeiro número: '{}'", numero);
            preencherCampoSeguro(firstNumberInput, numero);
        } catch (Exception e) {
            logger.error("Erro ao preencher primeiro número: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao preencher primeiro número", e);
        }
    }

    /**
     * Preenche o segundo número para soma
     */
    public void preencherSegundoNumero(String numero) {
        try {
            logger.info("Preenchendo segundo número: '{}'", numero);
            preencherCampoSeguro(secondNumberInput, numero);
        } catch (Exception e) {
            logger.error("Erro ao preencher segundo número: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao preencher segundo número", e);
        }    }

    // Método genérico agora substitui tanto Show Message quanto Get Sum
    // Use: clicarEmBotaoPorTexto("Show Message") ou clicarEmBotaoPorTexto("Get Sum")

    /**
     * Obtém o resultado da soma
     * Procura em diferentes locais possíveis onde o resultado pode aparecer
     */
    public String obterResultadoSoma() {
        try {
            // Lista de seletores possíveis para o resultado da soma
            By[] possiveisResultados = {
                By.id("addmessage"),
                By.id("displayvalue"),
                By.id("sum_result"),
                By.xpath("//div[@id='addmessage']"),
                By.xpath("//span[contains(@class, 'sum')]"),
                By.xpath("//div[contains(@class, 'sum')]"),
                By.xpath("//p[contains(@class, 'sum')]"),
                By.xpath("//*[contains(text(), '40')]"),
                By.xpath("//div[following-sibling::*[contains(@id, 'sum')]]")
            };
            
            // Tenta encontrar o resultado em qualquer um dos locais
            for (By seletor : possiveisResultados) {
                try {
                    if (elementoEstaPresente(seletor)) {
                        String resultado = obterTextoSeguro(seletor);
                        if (resultado != null && !resultado.trim().isEmpty()) {
                            logger.info("Resultado da soma encontrado com seletor {}: '{}'", seletor, resultado);
                            return resultado;
                        }
                    }
                } catch (Exception e) {
                    logger.debug("Erro ao tentar seletor {}: {}", seletor, e.getMessage());
                }
            }
            
            logger.warn("Resultado da soma não encontrado em nenhum local conhecido");
            return "";
            
        } catch (Exception e) {            logger.error("Erro ao obter resultado da soma: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Preenche o formulário de soma completo
     */
    public void preencherFormularioSoma(String num1, String num2) {
        try {
            preencherPrimeiroNumero(num1);
            preencherSegundoNumero(num2);
            logger.info("Formulário de soma preenchido com {} + {}", num1, num2);
        } catch (Exception e) {
            logger.error("Erro ao preencher formulário de soma: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao preencher formulário de soma", e);
        }
    }

    // =============================================================================
    // MÉTODOS DE LIMPEZA E VALIDAÇÃO
    // =============================================================================

    /**
     * Limpa todos os campos do formulário
     */
    public void limparTodosCampos() {
        try {
            WebElementUtils.buscarElementoVisivel(driver, messageInput).clear();
            WebElementUtils.buscarElementoVisivel(driver, firstNumberInput).clear();
            WebElementUtils.buscarElementoVisivel(driver, secondNumberInput).clear();
            logger.info("Todos os campos foram limpos");
        } catch (Exception e) {
            logger.error("Erro ao limpar campos: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao limpar campos", e);
        }
    }

    /**
     * Verifica se os campos estão visíveis e habilitados
     */
    public boolean camposEstaoVisiveis() {
        try {
            boolean mensagemVisivel = elementoEstaVisivel(messageInput);
            boolean primeiroNumeroVisivel = elementoEstaVisivel(firstNumberInput);
            boolean segundoNumeroVisivel = elementoEstaVisivel(secondNumberInput);
            
            logger.debug("Campos visíveis - Mensagem: {}, Num1: {}, Num2: {}", 
                        mensagemVisivel, primeiroNumeroVisivel, segundoNumeroVisivel);
            
            return mensagemVisivel && primeiroNumeroVisivel && segundoNumeroVisivel;
        } catch (Exception e) {
            logger.error("Erro ao verificar visibilidade dos campos: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Verifica se algum resultado está presente na página
     */
    public boolean resultadoEstaPresente() {
        try {
            boolean mensagemPresente = elementoEstaPresente(messageResult);
            boolean somaPresente = elementoEstaPresente(sumResult);
            
            logger.debug("Resultados presentes - Mensagem: {}, Soma: {}", 
                        mensagemPresente, somaPresente);
            
            return mensagemPresente || somaPresente;
        } catch (Exception e) {
            logger.error("Erro ao verificar presença de resultados: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Verifica se está na URL correta da página
     */
    public boolean estaEmPaginaFormularioSimples() {
        return urlAtualContem(SIMPLE_FORM_URL_PART);
    }

    /**
     * Obtém valores atuais dos campos de entrada
     */
    public String obterValorCampoMensagem() {
        return obterValorInput(messageInput);
    }

    public String obterValorPrimeiroNumero() {
        return obterValorInput(firstNumberInput);
    }

    public String obterValorSegundoNumero() {
        return obterValorInput(secondNumberInput);
    }    // =============================================================================
    // IMPLEMENTAÇÃO DO MÉTODO ABSTRATO
    // =============================================================================
    
    /**
     * Implementação do método abstrato da BasePage
     * Valida se a página carregou corretamente verificando elementos essenciais
     */    @Override
    protected boolean validarPaginaCarregada() {
        try {
            // Validação baseada nos elementos que realmente existem na página
            String currentUrl = driver.getCurrentUrl();
            boolean urlCorreta = currentUrl.contains("simple-form-demo");
            
            if (urlCorreta) {
                // Verifica elementos essenciais que sabemos que existem
                boolean campoMensagemPresente = elementoEstaPresente(messageInput);
                boolean botaoShowPresente = elementoEstaPresente(showMessageButton);
                boolean camposNumeroPresentes = elementoEstaPresente(firstNumberInput) && 
                                               elementoEstaPresente(secondNumberInput);
                
                boolean paginaValida = campoMensagemPresente && botaoShowPresente && camposNumeroPresentes;
                
                if (paginaValida) {
                    logger.info("Página do formulário simples validada com sucesso");
                    logger.debug("Elementos encontrados: mensagem={}, botão={}, números={}", 
                               campoMensagemPresente, botaoShowPresente, camposNumeroPresentes);
                } else {
                    logger.warn("Falha na validação da página - elementos essenciais ausentes");
                    logger.debug("Status dos elementos: mensagem={}, botão={}, números={}", 
                               campoMensagemPresente, botaoShowPresente, camposNumeroPresentes);
                }
                
                return paginaValida;
            }
            
            return false;
            
        } catch (Exception e) {
            logger.error("Erro durante validação da página do formulário: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Método temporário para debug - captura todos os elementos da página
     */
    public void debugElementosPagina() {
        try {
            logger.info("=== DEBUG COMPLETO DOS ELEMENTOS DA PÁGINA ===");
            
            // Busca todos os botões
            var botoes = driver.findElements(By.tagName("button"));
            logger.info("BOTÕES ENCONTRADOS: {}", botoes.size());
            for (int i = 0; i < botoes.size(); i++) {
                try {
                    String texto = botoes.get(i).getText();
                    String id = botoes.get(i).getAttribute("id");
                    String classes = botoes.get(i).getAttribute("class");
                    logger.info("Botão {}: Texto='{}', ID='{}', Class='{}'", i+1, texto, id, classes);
                } catch (Exception e) {
                    logger.warn("Erro ao obter info do botão {}: {}", i+1, e.getMessage());
                }
            }
            
            // Busca todos os inputs
            var inputs = driver.findElements(By.tagName("input"));
            logger.info("INPUTS ENCONTRADOS: {}", inputs.size());
            for (int i = 0; i < inputs.size(); i++) {
                try {
                    String id = inputs.get(i).getAttribute("id");
                    String type = inputs.get(i).getAttribute("type");
                    String placeholder = inputs.get(i).getAttribute("placeholder");
                    logger.info("Input {}: ID='{}', Type='{}', Placeholder='{}'", i+1, id, type, placeholder);
                } catch (Exception e) {
                    logger.warn("Erro ao obter info do input {}: {}", i+1, e.getMessage());
                }
            }
            
            // Busca todos os elementos com ID display
            var displays = driver.findElements(By.xpath("//*[contains(@id, 'display')]"));
            logger.info("ELEMENTOS COM 'display' NO ID: {}", displays.size());
            for (int i = 0; i < displays.size(); i++) {
                try {
                    String id = displays.get(i).getAttribute("id");
                    String tagName = displays.get(i).getTagName();
                    logger.info("Display {}: ID='{}', TagName='{}'", i+1, id, tagName);
                } catch (Exception e) {
                    logger.warn("Erro ao obter info do display {}: {}", i+1, e.getMessage());
                }
            }
            
            // Busca todos os h4 (títulos)
            var titulos = driver.findElements(By.tagName("h4"));
            logger.info("TÍTULOS H4 ENCONTRADOS: {}", titulos.size());
            for (int i = 0; i < titulos.size(); i++) {
                try {
                    String texto = titulos.get(i).getText();
                    logger.info("Título H4 {}: Texto='{}'", i+1, texto);
                } catch (Exception e) {
                    logger.warn("Erro ao obter texto do título {}: {}", i+1, e.getMessage());
                }
            }
            
            logger.info("=== FIM DO DEBUG DOS ELEMENTOS ===");
            
        } catch (Exception e) {
            logger.error("Erro durante debug dos elementos: {}", e.getMessage());
        }
    }
}
