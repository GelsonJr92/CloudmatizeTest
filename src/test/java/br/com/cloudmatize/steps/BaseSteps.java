package br.com.cloudmatize.steps;

import br.com.cloudmatize.config.ConfigManager;
import br.com.cloudmatize.driver.DriverFactory;
import br.com.cloudmatize.reports.ExtentTestManager;
import io.cucumber.java.pt.Dado;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Steps base comuns a todos os cenários de teste
 */
public class BaseSteps {
    private static final Logger logger = LogManager.getLogger(BaseSteps.class);    private final ConfigManager config = ConfigManager.getInstance();

    @Dado("que estou na pagina inicial do Selenium Playground")
    public void queEstouNaPaginaInicialDoSeleniumPlayground() {
        String baseUrl = config.getBaseUrl();
        DriverFactory.getDriver().get(baseUrl);
        logger.info("Navegando para a página inicial: {}", baseUrl);
        ExtentTestManager.logStep("Acessando página inicial do Selenium Playground");
    }
}
