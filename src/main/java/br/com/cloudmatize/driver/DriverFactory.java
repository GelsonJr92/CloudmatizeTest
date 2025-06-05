package br.com.cloudmatize.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.File;
import java.util.UUID;

public class DriverFactory {
    
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }
    
    public static WebDriver initDriver() {
        ChromeOptions options = new ChromeOptions();
        
        // Configurações para ambientes CI/CD
        String ciEnv = System.getenv("CI");
        String githubActions = System.getenv("GITHUB_ACTIONS");
          if ("true".equals(ciEnv) || "true".equals(githubActions)) {
            // Argumentos essenciais para CI
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-software-rasterizer");
            options.addArguments("--disable-background-timer-throttling");
            options.addArguments("--disable-backgrounding-occluded-windows");
            options.addArguments("--disable-renderer-backgrounding");
            options.addArguments("--disable-features=TranslateUI,VizDisplayCompositor");
            options.addArguments("--disable-ipc-flooding-protection");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-plugins");
            options.addArguments("--disable-web-security");
            options.addArguments("--allow-running-insecure-content");
            options.addArguments("--single-process");
            options.addArguments("--remote-debugging-port=9222");
              // Cria diretório único para cada instância do Chrome (compatível com Linux/Windows)
            String tempDir = System.getProperty("java.io.tmpdir");
            if (!tempDir.endsWith(File.separator)) {
                tempDir += File.separator;
            }
            String uniqueUserDataDir = tempDir + "chrome-user-data-" + UUID.randomUUID().toString();
            
            // Cria o diretório se não existir
            File userDataDir = new File(uniqueUserDataDir);
            if (!userDataDir.exists()) {
                userDataDir.mkdirs();
            }
            
            options.addArguments("--user-data-dir=" + uniqueUserDataDir);
            
            // Define tamanho da janela para headless
            options.addArguments("--window-size=1920,1080");
        }
        
        WebDriver driver = new ChromeDriver(options);
        
        // Maximiza a janela apenas se não estiver em headless
        if (!"true".equals(ciEnv) && !"true".equals(githubActions)) {
            try {
                driver.manage().window().maximize();
            } catch (Exception e) {
                // Em caso de erro, tenta ajustar para 1920x1080
                try {
                    driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
                } catch (Exception ignored) {}
            }
        }
        
        driverThreadLocal.set(driver);
        return driver;
    }
    
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}
