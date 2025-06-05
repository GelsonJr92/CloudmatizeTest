package br.com.cloudmatize.pages;

import br.com.cloudmatize.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object para a página inicial do Selenium Playground
 * Contém métodos para navegar e interagir com os links principais
 */
public class PlaygroundHomePage extends BasePage {
    
    private final ConfigManager config = ConfigManager.getInstance();
    
    // Localizadores dos links principais
    private final By selectDropdownLink = By.linkText("Select Dropdown List");
    private final By simpleFormLink = By.linkText("Simple Form Demo");
    private final By radioButtonsLink = By.linkText("Radio Buttons Demo");
    
    // Localizadores para validação da página
    private final By logoSeleniumPlayground = By.cssSelector(".navbar-brand");
    private final By menuNavegacao = By.cssSelector(".navbar-nav");
    private final By listaExemplos = By.cssSelector(".list-group");

    public PlaygroundHomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navega para a página inicial do Selenium Playground
     */
    public void navegarParaPaginaInicial() {
        try {
            String baseUrl = config.getBaseUrl();
            driver.get(baseUrl);
            logger.info("Navegando para a página inicial do Selenium Playground: {}", baseUrl);
            
            // Aguarda e valida se a página carregou corretamente
            if (!validarPaginaCarregada()) {
                throw new RuntimeException("Página inicial não carregou corretamente");
            }
            
            logger.info("Página inicial carregada com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao navegar para página inicial: {}", e.getMessage(), e);
            throw new RuntimeException("Falha na navegação para página inicial", e);
        }
    }

    /**
     * Clica no link "Select Dropdown List"
     */
    public void clicarEmSelectDropdownList() {
        try {
            logger.info("Clicando no link 'Select Dropdown List'");
            clicarSeguro(selectDropdownLink);
            logger.info("Navegação para 'Select Dropdown List' realizada com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao clicar em 'Select Dropdown List': {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao navegar para Select Dropdown List", e);
        }
    }

    /**
     * Clica no link "Simple Form Demo"
     */
    public void clicarEmSimpleFormDemo() {
        try {
            logger.info("Clicando no link 'Simple Form Demo'");
            clicarSeguro(simpleFormLink);
            logger.info("Navegação para 'Simple Form Demo' realizada com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao clicar em 'Simple Form Demo': {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao navegar para Simple Form Demo", e);
        }
    }

    /**
     * Clica no link "Radio Buttons Demo"
     */
    public void clicarEmRadioButtonsDemo() {
        try {
            logger.info("Clicando no link 'Radio Buttons Demo'");
            clicarSeguro(radioButtonsLink);
            logger.info("Navegação para 'Radio Buttons Demo' realizada com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao clicar em 'Radio Buttons Demo': {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao navegar para Radio Buttons Demo", e);
        }
    }

    /**
     * Verifica se todos os links principais estão presentes
     */
    public boolean verificarLinksEssenciaisPresentes() {
        try {
            boolean selectPresente = elementoEstaPresente(selectDropdownLink);
            boolean formPresente = elementoEstaPresente(simpleFormLink);
            boolean radioPresente = elementoEstaPresente(radioButtonsLink);
            
            logger.debug("Links principais - Select: {}, Form: {}, Radio: {}", 
                        selectPresente, formPresente, radioPresente);
            
            return selectPresente && formPresente && radioPresente;
        } catch (Exception e) {
            logger.error("Erro ao verificar links essenciais: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtém o título da página principal
     */
    public String obterTituloPaginaPrincipal() {
        return obterTituloPagina();
    }    /**
     * Verifica se está na URL correta da página inicial
     */
    public boolean estaEmPaginaInicial() {
        return urlAtualContem("lambdatest.com") && urlAtualContem("selenium-playground");
    }

    /**
     * Implementação do método abstrato da BasePage
     * Valida se a página carregou corretamente verificando elementos essenciais
     */
    @Override
    protected boolean validarPaginaCarregada() {
        try {
            // Aguarda elementos essenciais estarem presentes
            boolean logoPresente = elementoEstaPresente(logoSeleniumPlayground);
            boolean menuPresente = elementoEstaPresente(menuNavegacao);
            boolean listaPresente = elementoEstaPresente(listaExemplos);
            
            boolean paginaValida = logoPresente && menuPresente && listaPresente;
            
            if (paginaValida) {
                logger.debug("Página inicial validada com sucesso - todos os elementos essenciais presentes");
            } else {
                logger.warn("Falha na validação da página inicial - elementos ausentes: Logo: {}, Menu: {}, Lista: {}", 
                           logoPresente, menuPresente, listaPresente);
            }
            
            return paginaValida;
            
        } catch (Exception e) {
            logger.error("Erro durante validação da página inicial: {}", e.getMessage());
            return false;
        }
    }
}
