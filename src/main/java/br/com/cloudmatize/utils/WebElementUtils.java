package br.com.cloudmatize.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import java.util.List;

/**
 * Utilitários robustos para interações com elementos web
 * Suporte a esperas inteligentes, tratamento de erros e operações seguras
 */
public class WebElementUtils {
    private static final Logger logger = LogManager.getLogger(WebElementUtils.class);
    // Espera implícita será configurada globalmente no WebDriver
    // Removido timeout e polling interval fixos

    private WebElementUtils() {
        // Construtor privado para classe utilitária
    }

    // =============================================================================
    // MÉTODOS DE BUSCA DIRETA (usando espera implícita do WebDriver)
    // =============================================================================

    /**
     * Busca elemento presente e visível (usa espera implícita do WebDriver)
     */
    public static WebElement buscarElementoVisivel(WebDriver driver, By localizador) {
        WebElement elemento = driver.findElement(localizador);
        if (!elemento.isDisplayed()) {
            throw new RuntimeException("Elemento encontrado mas não está visível: " + localizador);
        }
        return elemento;
    }

    /**
     * Busca elemento presente e habilitado (usa espera implícita do WebDriver)
     */
    public static WebElement buscarElementoClicavel(WebDriver driver, By localizador) {
        WebElement elemento = driver.findElement(localizador);
        if (!elemento.isDisplayed() || !elemento.isEnabled()) {
            throw new RuntimeException("Elemento não está clicável: " + localizador);
        }
        return elemento;
    }

    // =============================================================================
    // MÉTODOS DE VERIFICAÇÃO SEGURA
    // =============================================================================

    /**
     * Verifica se elemento está presente na página
     */
    public static boolean elementoEstaPresente(WebDriver driver, By localizador) {
        try {
            driver.findElement(localizador);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Verifica se elemento está visível
     */
    public static boolean elementoEstaVisivel(WebDriver driver, By localizador) {
        try {
            WebElement elemento = driver.findElement(localizador);
            return elemento.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Verifica se elemento está habilitado
     */
    public static boolean elementoEstaHabilitado(WebDriver driver, By localizador) {
        try {
            WebElement elemento = driver.findElement(localizador);
            return elemento.isEnabled();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Verifica se elemento está selecionado (radio/checkbox)
     */
    public static boolean elementoEstaSelecionado(WebDriver driver, By localizador) {
        try {
            WebElement elemento = driver.findElement(localizador);
            return elemento.isSelected();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    // =============================================================================
    // MÉTODOS DE INTERAÇÃO ROBUSTA
    // =============================================================================

    /**
     * Clica no elemento com espera automática
     */
    public static void clicarElemento(WebDriver driver, By localizador) {
        try {
            WebElement elemento = buscarElementoClicavel(driver, localizador);
            elemento.click();
            logger.debug("Clique realizado no elemento: {}", localizador);
        } catch (Exception e) {
            logger.error("Erro ao clicar no elemento: {}", localizador, e);
            throw new RuntimeException("Falha ao clicar no elemento: " + localizador, e);
        }
    }

    /**
     * Clica usando JavaScript (útil para elementos sobrepostos)
     */
    public static void clicarComJavaScript(WebDriver driver, By localizador) {
        try {
            WebElement elemento = buscarElementoVisivel(driver, localizador);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", elemento);
            logger.debug("Clique JavaScript realizado no elemento: {}", localizador);
        } catch (Exception e) {
            logger.error("Erro ao clicar com JavaScript no elemento: {}", localizador, e);
            throw new RuntimeException("Falha ao clicar com JavaScript no elemento: " + localizador, e);
        }
    }

    /**
     * Limpa campo e insere texto com validação
     */
    public static void preencherCampo(WebDriver driver, By localizador, String texto) {
        try {
            WebElement elemento = buscarElementoVisivel(driver, localizador);
            elemento.clear();
            elemento.sendKeys(texto);
            // Validação do texto inserido
            String valorAtual = elemento.getAttribute("value");
            if (!texto.equals(valorAtual)) {
                logger.warn("Texto inserido difere do esperado. Esperado: '{}', Atual: '{}'", texto, valorAtual);
            }
            logger.debug("Campo preenchido com sucesso: {} = '{}'", localizador, texto);
        } catch (Exception e) {
            logger.error("Erro ao preencher campo: {}", localizador, e);
            throw new RuntimeException("Falha ao preencher campo: " + localizador, e);
        }
    }

    // =============================================================================
    // MÉTODOS DE SCROLL E NAVEGAÇÃO
    // =============================================================================

    /**
     * Rola página até o elemento ficar visível
     */
    public static void rolarParaElemento(WebDriver driver, By localizador) {
        try {
            WebElement elemento = buscarElementoVisivel(driver, localizador);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", elemento);
            logger.debug("Scroll realizado até o elemento: {}", localizador);
        } catch (Exception e) {
            logger.error("Erro ao rolar para elemento: {}", localizador, e);
            throw new RuntimeException("Falha ao rolar para elemento: " + localizador, e);
        }
    }

    // =============================================================================
    // MÉTODOS PARA DROPDOWN/SELECT
    // =============================================================================

    /**
     * Seleciona opção em dropdown por texto visível
     */
    public static void selecionarDropdownPorTexto(WebDriver driver, By localizador, String texto) {
        try {
            WebElement elementoSelect = buscarElementoVisivel(driver, localizador);
            Select select = new Select(elementoSelect);
            select.selectByVisibleText(texto);
            logger.debug("Opção '{}' selecionada no dropdown: {}", texto, localizador);
        } catch (Exception e) {
            logger.error("Erro ao selecionar opção '{}' no dropdown: {}", texto, localizador, e);
            throw new RuntimeException("Falha ao selecionar opção no dropdown: " + localizador, e);
        }
    }

    /**
     * Seleciona opção em dropdown por valor
     */
    public static void selecionarDropdownPorValor(WebDriver driver, By localizador, String valor) {
        try {
            WebElement elementoSelect = buscarElementoVisivel(driver, localizador);
            Select select = new Select(elementoSelect);
            select.selectByValue(valor);
            logger.debug("Valor '{}' selecionado no dropdown: {}", valor, localizador);
        } catch (Exception e) {
            logger.error("Erro ao selecionar valor '{}' no dropdown: {}", valor, localizador, e);
            throw new RuntimeException("Falha ao selecionar valor no dropdown: " + localizador, e);
        }
    }

    /**
     * Obtém todas as opções de um dropdown
     */
    public static List<WebElement> obterOpcoesDropdown(WebDriver driver, By localizador) {
        try {
            WebElement elementoSelect = buscarElementoVisivel(driver, localizador);
            Select select = new Select(elementoSelect);
            return select.getOptions();
        } catch (Exception e) {
            logger.error("Erro ao obter opções do dropdown: {}", localizador, e);
            throw new RuntimeException("Falha ao obter opções do dropdown: " + localizador, e);
        }
    }

    /**
     * Obtém opção selecionada em dropdown
     */
    public static String obterOpcaoSelecionadaDropdown(WebDriver driver, By localizador) {
        try {
            WebElement elementoSelect = buscarElementoVisivel(driver, localizador);
            Select select = new Select(elementoSelect);
            return select.getFirstSelectedOption().getText();
        } catch (Exception e) {
            logger.error("Erro ao obter opção selecionada do dropdown: {}", localizador, e);
            throw new RuntimeException("Falha ao obter opção selecionada do dropdown: " + localizador, e);
        }
    }

    // =============================================================================
    // MÉTODOS DE BUSCA POR TEXTO
    // =============================================================================

    /**
     * Encontra botão pelo texto exato - método genérico para qualquer página
     * Tenta múltiplos seletores para encontrar botões com diferentes estruturas HTML
     */
    public static WebElement encontrarBotaoPorTexto(WebDriver driver, String textoBotao) {
        // Não há mais timeout customizado, usa espera implícita do WebDriver
        return encontrarBotaoPorTexto(driver, textoBotao, 0);
    }

    /**
     * Encontra botão pelo texto exato com timeout customizado
     * Tenta múltiplos seletores para máxima compatibilidade
     */
    public static WebElement encontrarBotaoPorTexto(WebDriver driver, String textoBotao, int timeoutSegundos) {
        logger.info("Procurando botão com texto: '{}'", textoBotao);
        // Lista de seletores para tentar (do mais específico para o mais genérico)
        String[] seletores = {
            String.format("//button[normalize-space(text())='%s']", textoBotao),
            String.format("//input[@type='button' and @value='%s']", textoBotao),
            String.format("//input[@type='submit' and @value='%s']", textoBotao),
            String.format("//button[contains(normalize-space(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), '%s')]", textoBotao.toLowerCase()),
            String.format("//input[@type='button' and contains(normalize-space(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), '%s')]", textoBotao.toLowerCase()),
            String.format("//input[@type='submit' and contains(normalize-space(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), '%s')]", textoBotao.toLowerCase()),
            String.format("//*[contains(@class, 'btn') and normalize-space(text())='%s']", textoBotao),
            String.format("//*[@role='button' and normalize-space(text())='%s']", textoBotao),
            String.format("//a[normalize-space(text())='%s']", textoBotao),
            String.format("//*[contains(@class, 'button') and normalize-space(text())='%s']", textoBotao),
            String.format("//*[contains(@class, 'btn-') and normalize-space(text())='%s']", textoBotao)
        };
        for (String seletor : seletores) {
            try {
                By localizador = By.xpath(seletor);
                WebElement elemento = driver.findElement(localizador);
                if (elemento.isDisplayed() && elemento.isEnabled()) {
                    logger.info("Botão '{}' encontrado com seletor: {}", textoBotao, seletor);
                    return elemento;
                }
            } catch (NoSuchElementException e) {
                logger.debug("Seletor não encontrou o botão '{}': {}", textoBotao, seletor);
            }
        }
        logger.warn("Botão '{}' não encontrado. Listando todos os botões disponíveis:", textoBotao);
        listarTodosBotoesDisponiveis(driver);
        throw new RuntimeException("Botão com texto '" + textoBotao + "' não encontrado na página");
    }

    /**
     * Lista todos os botões disponíveis na página para debug
     */
    public static void listarTodosBotoesDisponiveis(WebDriver driver) {
        try {
            logger.info("=== LISTANDO TODOS OS BOTÕES DISPONÍVEIS ===");
            
            // Busca por todos os tipos de elementos que podem ser botões
            String[] seletoresBotoes = {
                "//button",
                "//input[@type='button']", 
                "//input[@type='submit']",
                "//*[contains(@class, 'btn')]",
                "//*[@role='button']",
                "//a[contains(@class, 'button')]"
            };
            
            int contador = 0;
            for (String seletor : seletoresBotoes) {
                try {
                    List<WebElement> elementos = driver.findElements(By.xpath(seletor));
                    for (WebElement elemento : elementos) {
                        contador++;
                        String texto = elemento.getText();
                        String value = elemento.getAttribute("value");
                        String id = elemento.getAttribute("id");
                        String classes = elemento.getAttribute("class");
                        
                        logger.info("Botão {}: Texto='{}', Value='{}', ID='{}', Classes='{}'", 
                            contador, texto, value, id, classes);
                    }
                } catch (Exception e) {
                    logger.debug("Erro ao buscar elementos com seletor: {}", seletor);
                }
            }
            
            if (contador == 0) {
                logger.warn("Nenhum botão encontrado na página!");
            } else {
                logger.info("Total de {} botões encontrados", contador);
            }
            
        } catch (Exception e) {
            logger.error("Erro ao listar botões disponíveis", e);
        }
    }

    // =============================================================================
    // MÉTODOS DE OBTENÇÃO DE DADOS
    // =============================================================================

    /**
     * Obtém texto do elemento de forma segura
     */
    public static String obterTexto(WebDriver driver, By localizador) {
        try {
            WebElement elemento = buscarElementoVisivel(driver, localizador);
            String texto = elemento.getText().trim().replaceAll("\\s+", " ");
            logger.debug("Texto obtido do elemento {}: '{}'", localizador, texto);
            return texto;
        } catch (Exception e) {
            logger.error("Erro ao obter texto do elemento: {}", localizador, e);
            return "";
        }
    }

    /**
     * Obtém valor do atributo do elemento
     */
    public static String obterAtributo(WebDriver driver, By localizador, String atributo) {
        try {
            WebElement elemento = buscarElementoVisivel(driver, localizador);
            String valor = elemento.getAttribute(atributo);
            logger.debug("Atributo '{}' do elemento {}: '{}'", atributo, localizador, valor);
            return valor != null ? valor : "";
        } catch (Exception e) {
            logger.error("Erro ao obter atributo '{}' do elemento: {}", atributo, localizador, e);
            return "";
        }
    }

    /**
     * Obtém valor de campo de input
     */
    public static String obterValorInput(WebDriver driver, By localizador) {
        return obterAtributo(driver, localizador, "value");
    }

    // =============================================================================
    // MÉTODOS UTILITÁRIOS JAVASCRIPT
    // =============================================================================

    /**
     * Executa JavaScript no elemento
     */
    public static Object executarJavaScript(WebDriver driver, String script, By localizador) {
        try {
            WebElement elemento = buscarElementoVisivel(driver, localizador);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            Object resultado = js.executeScript(script, elemento);
            logger.debug("JavaScript executado no elemento: {}", localizador);
            return resultado;
        } catch (Exception e) {
            logger.error("Erro ao executar JavaScript no elemento: {}", localizador, e);
            throw new RuntimeException("Falha ao executar JavaScript no elemento: " + localizador, e);
        }
    }

    /**
     * Executa ação customizada com retry automático
     */
    // Removido método executarComRetry pois não faz sentido com espera implícita
}
