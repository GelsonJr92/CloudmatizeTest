package br.com.cloudmatize.steps;

import br.com.cloudmatize.pages.SimpleFormPage;
import br.com.cloudmatize.pages.SelectDropdownPage;
import br.com.cloudmatize.pages.RadioButtonPage;
import br.com.cloudmatize.driver.DriverFactory;
import br.com.cloudmatize.utils.WebElementUtils;
import br.com.cloudmatize.reports.ExtentTestManager;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Steps comuns que podem ser reutilizados em diferentes cenários
 * Contém ações genéricas de navegação e validações compartilhadas
 * Com métodos dinâmicos para localizar elementos por texto
 */
public class CommonSteps {
    private static final Logger logger = LogManager.getLogger(CommonSteps.class);
    
    private SimpleFormPage getSimpleFormPage() {
        return new SimpleFormPage(DriverFactory.getDriver());
    }
    
    private SelectDropdownPage getSelectDropdownPage() {
        return new SelectDropdownPage(DriverFactory.getDriver());
    }
    
    private RadioButtonPage getRadioButtonPage() {
        return new RadioButtonPage(DriverFactory.getDriver());
    }
  

    @Quando("eu navego para a pagina {string}")
    public void euNavegoParaAPagina(String pagina) {
        logger.info("Navegando para a página: '{}'", pagina);
        
        try {
            switch (pagina) {
                case "Simple Form Demo":
                    getSimpleFormPage().navegarParaPaginaFormularioSimples();
                    break;
                case "Select Dropdown List":
                    getSelectDropdownPage().navegarParaPaginaSelectDropdown();
                    break;
                case "Radio Buttons Demo":
                    getRadioButtonPage().navegarParaPaginaRadioButtons();
                    break;
                default:
                    logger.error("Página não reconhecida: '{}'", pagina);
                    throw new IllegalArgumentException("Página não reconhecida: " + pagina);
            }
            
            ExtentTestManager.logStep("Navegação realizada para a página: " + pagina);
            logger.debug("Navegação para '{}' concluída com sucesso", pagina);
            
        } catch (Exception e) {
            ExtentTestManager.logFail("Falha na navegação para a página: " + pagina);
            logger.error("Erro ao navegar para a página '{}': {}", pagina, e.getMessage(), e);
            throw e;
        }
    }
  
    
    @Quando("eu clico no elemento que contem o texto {string}")
    public void euClicoNoElementoQueContemOTexto(String texto) {
        logger.info("Clicando no elemento que contém o texto: '{}'", texto);
        
        try {
            By localizador = By.xpath("//*[contains(text(),'" + texto + "')]");
            WebElementUtils.clicarElemento(DriverFactory.getDriver(), localizador);
            ExtentTestManager.logStep("Clicado no elemento com texto: " + texto);
        } catch (Exception e) {
            ExtentTestManager.logFail("Falha ao clicar no elemento com texto: " + texto);
            logger.error("Erro ao clicar no elemento com texto '{}': {}", texto, e.getMessage(), e);
            throw e;
        }
    }
    
    @Entao("eu devo ver o texto {string} na pagina")
    public void euDevoVerOTextoNaPagina(String textoEsperado) {
        logger.info("Validando se o texto '{}' está presente na página", textoEsperado);
        
        try {
            By localizador = By.xpath("//*[contains(text(),'" + textoEsperado + "')]");
            boolean textoPresente = WebElementUtils.elementoEstaPresente(DriverFactory.getDriver(), localizador);
            
            Assert.assertTrue("O texto '" + textoEsperado + "' deveria estar presente na página", textoPresente);
            ExtentTestManager.logPass("Texto encontrado na página: " + textoEsperado);
            
        } catch (AssertionError e) {
            ExtentTestManager.logFail("Texto não encontrado na página: " + textoEsperado);
            logger.error("Texto '{}' não foi encontrado na página", textoEsperado);
            throw e;
        } catch (Exception e) {
            ExtentTestManager.logFail("Erro ao validar texto na página: " + textoEsperado);
            logger.error("Erro ao validar texto '{}' na página: {}", textoEsperado, e.getMessage(), e);
            throw e;
        }
    }
}
