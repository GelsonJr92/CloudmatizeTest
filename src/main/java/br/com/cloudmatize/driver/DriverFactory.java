package br.com.cloudmatize.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.File;
import java.util.UUID;
import java.util.Random;

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
            System.out.println("=== CONFIGURANDO CHROME PARA CI ===");
            
            // Argumentos essenciais para CI sem conflitos
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
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--disable-features=VizDisplayCompositor");
            
            // REMOVIDO: --single-process (causa conflitos em CI paralelos)
            // REMOVIDO: --remote-debugging-port=9222 (porta fixa causa conflitos)
            
            // Gera porta única para debugging (evita conflitos entre instâncias)
            int debugPort = 9222 + new Random().nextInt(1000);
            options.addArguments("--remote-debugging-port=" + debugPort);
            
            // Cria diretório único para cada instância do Chrome
            String tempDir = System.getProperty("java.io.tmpdir");
            if (!tempDir.endsWith(File.separator)) {
                tempDir += File.separator;
            }
            String uniqueId = UUID.randomUUID().toString().substring(0, 8);
            String uniqueUserDataDir = tempDir + "chrome-user-data-" + uniqueId;
            
            // Cria o diretório se não existir
            File userDataDir = new File(uniqueUserDataDir);
            if (!userDataDir.exists()) {
                boolean created = userDataDir.mkdirs();
                System.out.println("Diretório criado: " + uniqueUserDataDir + " - Sucesso: " + created);
            }
            
            options.addArguments("--user-data-dir=" + uniqueUserDataDir);
            options.addArguments("--window-size=1920,1080");
            
            System.out.println("Chrome configurado para CI - Porta: " + debugPort + " - UserData: " + uniqueUserDataDir);
        }        
        WebDriver driver = null;
        try {
            System.out.println("=== INICIANDO CHROME DRIVER ===");
            driver = new ChromeDriver(options);
            
            // Configurar timeouts para estabilidade
            driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(30));
            driver.manage().timeouts().scriptTimeout(java.time.Duration.ofSeconds(30));
            
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
            
            System.out.println("=== CHROME DRIVER INICIADO COM SUCESSO ===");
            
        } catch (Exception e) {
            System.err.println("=== ERRO AO INICIAR CHROME DRIVER ===");
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Falha ao inicializar o WebDriver: " + e.getMessage(), e);
        }
        
        driverThreadLocal.set(driver);
        return driver;
    }
    
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                System.out.println("=== FINALIZANDO CHROME DRIVER ===");
                driver.quit();
                System.out.println("=== CHROME DRIVER FINALIZADO ===");
            } catch (Exception e) {
                System.err.println("Erro ao finalizar driver: " + e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}
