package br.com.cloudmatize.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Gerenciador de relatórios Extent Reports
 * Responsável por criar, configurar e gerenciar os relatórios de teste
 */
public class ExtentTestManager {
    private static final Logger logger = LogManager.getLogger(ExtentTestManager.class);
    
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final String REPORTS_PATH = "test-output/extent-reports/";
    private static final String SCREENSHOTS_PATH = "test-output/screenshots/";
    
    static {
        setupExtentReports();
    }
    
    /**
     * Configura o ExtentReports
     */
    private static void setupExtentReports() {
        if (extent == null) {
            // Criar diretório de relatórios se não existir
            File reportsDir = new File(REPORTS_PATH);
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }
            
            // Configurar o reporter
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportName = "TestReport_" + timestamp + ".html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORTS_PATH + reportName);
            
            // Configurações do reporter
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setReportName("Cloudmatize Test Report");
            sparkReporter.config().setDocumentTitle("Automation Test Results");
            sparkReporter.config().setTimeStampFormat("dd/MM/yyyy HH:mm:ss");
            
            // Inicializar ExtentReports
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            
            // Informações do sistema
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
            extent.setSystemInfo("Environment", "Test");
            
            logger.info("ExtentReports configurado com sucesso. Relatório: {}", reportName);
        }
    }
    
    /**
     * Inicia um novo teste
     */
    public static synchronized void startTest(String testName, String description) {
        ExtentTest test = extent.createTest(testName, description);
        extentTest.set(test);
        logger.debug("Teste iniciado: {}", testName);
    }
    
    /**
     * Obtém o teste atual
     */
    public static ExtentTest getTest() {
        return extentTest.get();
    }
    
    /**
     * Loga um passo no teste
     */
    public static void logStep(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.info(message);
            logger.debug("Step logado: {}", message);
        }
    }
    
    /**
     * Loga um passo de sucesso
     */
    public static void logPass(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.pass(message);
            logger.debug("Passo de sucesso logado: {}", message);
        }
    }
    
    /**
     * Loga um passo de falha
     */
    public static void logFail(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.fail(message);
            logger.debug("Passo de falha logado: {}", message);
        }
    }
    
    /**
     * Loga um passo de falha com exception
     */
    public static void logFail(String message, Throwable throwable) {
        ExtentTest test = getTest();
        if (test != null) {
            test.fail(message).fail(throwable);
            logger.debug("Passo de falha com exception logado: {}", message);
        }
    }
    
    /**
     * Loga aviso
     */
    public static void logWarning(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.warning(message);
            logger.debug("Aviso logado: {}", message);
        }
    }
    
    /**
     * Captura screenshot e anexa ao relatório
     */
    public static void addScreenshot(WebDriver driver, String description) {
        try {
            ExtentTest test = getTest();
            if (test != null && driver != null) {
                // Criar diretório de screenshots se não existir
                File screenshotsDir = new File(SCREENSHOTS_PATH);
                if (!screenshotsDir.exists()) {
                    screenshotsDir.mkdirs();
                }
                
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
                String screenshotName = "screenshot_" + timestamp + ".png";
                
                // Capturar screenshot
                TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
                String base64Screenshot = takesScreenshot.getScreenshotAs(OutputType.BASE64);
                
                // Anexar ao relatório
                test.info(description, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                logger.debug("Screenshot capturado e anexado: {}", screenshotName);
            }
        } catch (Exception e) {
            logger.error("Erro ao capturar screenshot: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Finaliza todos os testes e gera o relatório
     */
    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
            logger.info("Relatório ExtentReports finalizado");
        }
    }
    
    /**
     * Remove o teste atual da thread
     */
    public static void removeTest() {
        extentTest.remove();
    }
    
    /**
     * Obtém o caminho do diretório de relatórios
     */
    public static String getReportsPath() {
        return REPORTS_PATH;
    }
    
    /**
     * Obtém o caminho do diretório de screenshots
     */
    public static String getScreenshotsPath() {
        return SCREENSHOTS_PATH;
    }
}
