package br.com.cloudmatize.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverFactory {
    
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }
    
    public static WebDriver initDriver() {
        WebDriver driver = new ChromeDriver();
        // Maximiza a janela (ou ajusta para headless, se necessário)
        try {
            // Se quiser condicionar ao headless, adicione lógica aqui
            driver.manage().window().maximize();
        } catch (Exception e) {
            // Em caso de erro, tenta ajustar para 1920x1080
            try {
                driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
            } catch (Exception ignored) {}
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
