package br.com.cloudmatize.pages;

import br.com.cloudmatize.utils.WebElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Classe base robusta para todos os Page Objects
 * Implementa funcionalidades comuns com tratamento de erro e esperas inteligentes
 */
public abstract class BasePage {
    protected final WebDriver driver;
    protected final Logger logger = LogManager.getLogger(getClass());

    public BasePage(WebDriver driver) {
        this.driver = driver;
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver não pode ser null");
        }
    }

    // =============================================================================
    // MÉTODOS DE NAVEGAÇÃO E VALIDAÇÃO
    // =============================================================================

    /**
     * Verifica se estamos na página correta comparando a URL
     */
    protected boolean urlAtualContem(String urlEsperada) {
        try {
            String currentUrl = obterUrlAtual();
            boolean isCorrectUrl = currentUrl.contains(urlEsperada);
            logger.debug("URL atual: {} | URL esperada: {} | Corresponde: {}", currentUrl, urlEsperada, isCorrectUrl);
            return isCorrectUrl;
        } catch (Exception e) {
            logger.error("Erro ao verificar URL: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtém URL atual de forma segura
     */
    protected String obterUrlAtual() {
        try {
            return driver.getCurrentUrl();
        } catch (Exception e) {
            logger.error("Erro ao obter URL atual: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Obtém título da página de forma segura
     */
    protected String obterTituloPagina() {
        try {
            return driver.getTitle();
        } catch (Exception e) {
            logger.error("Erro ao obter título da página: {}", e.getMessage());
            return "";
        }
    }

    // =============================================================================
    // MÉTODOS DE BUSCA E ESPERA DE ELEMENTOS
    // =============================================================================

    // Métodos de espera explícita removidos. Usar apenas espera implícita global.

    // =============================================================================
    // MÉTODOS DE VERIFICAÇÃO
    // =============================================================================

    /**
     * Verifica se elemento está presente
     */
    protected boolean elementoEstaPresente(By localizador) {
        return WebElementUtils.elementoEstaPresente(driver, localizador);
    }

    /**
     * Verifica se elemento está visível
     */
    protected boolean elementoEstaVisivel(By localizador) {
        return WebElementUtils.elementoEstaVisivel(driver, localizador);
    }

    /**
     * Verifica se elemento está habilitado
     */
    protected boolean elementoEstaHabilitado(By localizador) {
        return WebElementUtils.elementoEstaHabilitado(driver, localizador);
    }

    /**
     * Verifica se elemento está selecionado (radio/checkbox)
     */
    protected boolean elementoEstaSelecionado(By localizador) {
        return WebElementUtils.elementoEstaSelecionado(driver, localizador);
    }

    // =============================================================================
    // MÉTODOS DE INTERAÇÃO COM ELEMENTOS
    // =============================================================================

    /**
     * Clica em elemento de forma segura com espera automática
     */
    protected void clicarSeguro(By localizador) {
        try {
            WebElementUtils.clicarElemento(driver, localizador);
            logger.debug("Clique realizado no elemento: {}", localizador);
        } catch (Exception e) {
            logger.error("Erro ao clicar no elemento: {}", localizador, e);
            throw e;
        }
    }

    /**
     * Clica usando JavaScript (útil para elementos sobrepostos)
     */
    protected void clicarComJavaScript(By localizador) {
        try {
            WebElementUtils.clicarComJavaScript(driver, localizador);
            logger.debug("Clique JavaScript realizado no elemento: {}", localizador);
        } catch (Exception e) {
            logger.error("Erro ao clicar com JavaScript no elemento: {}", localizador, e);
            throw e;
        }
    }

    /**
     * Preenche campo de texto de forma segura
     */
    protected void preencherCampoSeguro(By localizador, String texto) {
        try {
            WebElementUtils.preencherCampo(driver, localizador, texto);
            logger.debug("Campo preenchido: {} com texto: '{}'", localizador, texto);
        } catch (Exception e) {
            logger.error("Erro ao preencher campo {}: {}", localizador, e.getMessage(), e);
            throw e;
        }
    }

    // Métodos de espera artificial removidos. Usar apenas espera implícita global.

    // =============================================================================
    // MÉTODOS DE OBTENÇÃO DE DADOS
    // =============================================================================

    /**
     * Obtém texto de elemento de forma segura
     */
    protected String obterTextoSeguro(By localizador) {
        try {
            String texto = WebElementUtils.obterTexto(driver, localizador);
            logger.debug("Texto obtido do elemento {}: '{}'", localizador, texto);
            return texto;
        } catch (Exception e) {
            logger.warn("Elemento não encontrado ou não visível: {}. Retornando texto vazio.", localizador);
            return "";
        }
    }

    /**
     * Obtém valor de input de forma segura
     */
    protected String obterValorInput(By localizador) {
        try {
            String valor = WebElementUtils.obterValorInput(driver, localizador);
            logger.debug("Valor obtido do input {}: '{}'", localizador, valor);
            return valor;
        } catch (Exception e) {
            logger.warn("Erro ao obter valor do input: {}. Retornando valor vazio.", localizador);
            return "";
        }
    }

    /**
     * Obtém atributo de elemento de forma segura
     */
    protected String obterAtributo(By localizador, String atributo) {
        try {
            String valor = WebElementUtils.obterAtributo(driver, localizador, atributo);
            logger.debug("Atributo '{}' obtido do elemento {}: '{}'", atributo, localizador, valor);
            return valor;
        } catch (Exception e) {
            logger.warn("Erro ao obter atributo '{}' do elemento: {}. Retornando valor vazio.", atributo, localizador);
            return "";
        }
    }

    // =============================================================================
    // MÉTODOS DE SCROLL E NAVEGAÇÃO
    // =============================================================================

    /**
     * Rola página até o elemento
     */
    protected void rolarParaElemento(By localizador) {
        try {
            WebElementUtils.rolarParaElemento(driver, localizador);
            logger.debug("Scroll realizado até o elemento: {}", localizador);
        } catch (Exception e) {
            logger.error("Erro ao rolar para elemento: {}", localizador, e);
            throw e;
        }
    }

    // =============================================================================
    // MÉTODOS PARA DROPDOWN/SELECT
    // =============================================================================

    /**
     * Seleciona opção em dropdown por texto
     */
    protected void selecionarDropdownPorTexto(By localizador, String texto) {
        try {
            WebElementUtils.selecionarDropdownPorTexto(driver, localizador, texto);
            logger.debug("Opção '{}' selecionada no dropdown: {}", texto, localizador);
        } catch (Exception e) {
            logger.error("Erro ao selecionar opção '{}' no dropdown: {}", texto, localizador, e);
            throw e;
        }
    }

    /**
     * Seleciona opção em dropdown por valor
     */
    protected void selecionarDropdownPorValor(By localizador, String valor) {
        try {
            WebElementUtils.selecionarDropdownPorValor(driver, localizador, valor);
            logger.debug("Valor '{}' selecionado no dropdown: {}", valor, localizador);
        } catch (Exception e) {
            logger.error("Erro ao selecionar valor '{}' no dropdown: {}", valor, localizador, e);
            throw e;
        }
    }

    /**
     * Obtém opção selecionada em dropdown
     */
    protected String obterOpcaoSelecionadaDropdown(By localizador) {
        try {
            String opcao = WebElementUtils.obterOpcaoSelecionadaDropdown(driver, localizador);
            logger.debug("Opção selecionada no dropdown {}: '{}'", localizador, opcao);
            return opcao;
        } catch (Exception e) {
            logger.error("Erro ao obter opção selecionada do dropdown: {}", localizador, e);
            return "";
        }
    }

    // =============================================================================
    // MÉTODOS DE AGUARDA E VALIDAÇÃO
    // =============================================================================

    // Métodos de aguarda explícita removidos. Usar apenas espera implícita global.

    /**
     * Valida se a página carregou corretamente verificando elementos essenciais
     * Deve ser implementado por cada page específica
     */
    protected abstract boolean validarPaginaCarregada();
}
