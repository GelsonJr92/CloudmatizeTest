package br.com.cloudmatize.hooks;

import br.com.cloudmatize.driver.DriverFactory;
import br.com.cloudmatize.config.ConfigManager;
import br.com.cloudmatize.reports.ExtentTestManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class TestHooks {
    private static final Logger logger = LogManager.getLogger(TestHooks.class);    private final ConfigManager config = ConfigManager.getInstance();

    @Before
    public void beforeScenario(Scenario scenario) {
        logger.info("=== INICIANDO CENÁRIO: {} ===", scenario.getName());
        // Inicializa o WebDriver para o cenário
        DriverFactory.initDriver();
        // Configurar driver baseado na configuração
        String browser = config.getBrowser();
        boolean headless = config.isHeadless();
        logger.info("Configurando browser: {} - Headless: {}", browser, headless);
        // Inicializar o teste no ExtentReports
        ExtentTestManager.startTest(scenario.getName(), "Cenário de teste automatizado");
        // Log das tags do cenário
        if (!scenario.getSourceTagNames().isEmpty()) {
            logger.info("Tags do cenário: {}", scenario.getSourceTagNames());
            ExtentTestManager.getTest().assignCategory(scenario.getSourceTagNames().toArray(new String[0]));
        }
    }    @After
    public void afterScenario(Scenario scenario) {
        logger.info("=== FINALIZANDO CENÁRIO: {} ===", scenario.getName());
          try {
            WebDriver driver = DriverFactory.getDriver();
            
            if (scenario.isFailed()) {
                logger.error("Cenário falhou: {}", scenario.getName());
                
                // Capturar screenshot em caso de falha
                if (driver != null) {
                    byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    scenario.attach(screenshot, "image/png", "Screenshot da Falha");
                    
                    // Anexar screenshot ao ExtentReports
                    String screenshotBase64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                    ExtentTestManager.getTest().fail("Cenário falhou")
                        .addScreenCaptureFromBase64String(screenshotBase64, "Screenshot da Falha");
                }
            } else {
                logger.info("Cenário executado com sucesso: {}", scenario.getName());
                ExtentTestManager.getTest().pass("Cenário executado com sucesso");
            }
            
            // Log do status final
            logger.info("Status do cenário: {}", scenario.getStatus());
            
        } catch (Exception e) {
            logger.error("Erro durante a finalização do cenário: {}", e.getMessage(), e);
        } finally {
            // Fechar o driver
            DriverFactory.quitDriver();
            logger.info("Driver finalizado para o cenário: {}", scenario.getName());
        }
    }
}
