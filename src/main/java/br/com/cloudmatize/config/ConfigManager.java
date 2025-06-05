package br.com.cloudmatize.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe singleton robusta para gerenciar as configurações da aplicação
 * Carrega propriedades de um arquivo config.properties com múltiplas estratégias de localização
 * Prioriza system properties sobre as do arquivo de configuração
 * Suporta diferentes caminhos e carregamento via classpath
 */
public class ConfigManager {
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static volatile ConfigManager instance;
    private final Properties properties = new Properties();
    
    private static final String[] CONFIG_PATHS = {
        "src/main/resources/config.properties",
        "src/test/resources/config.properties", 
        "config.properties",
        "/config.properties"
    };

    private ConfigManager() {
        loadProperties();
        logLoadedConfiguration();
    }

    /**
     * Thread-safe singleton com double-checked locking
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    /**
     * Carrega propriedades com múltiplas estratégias de localização
     */
    private void loadProperties() {
        boolean loaded = false;
        
        // Primeira tentativa: via classpath
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) {
                properties.load(is);
                logger.info("Configurações carregadas via classpath: config.properties");
                loaded = true;
            }
        } catch (IOException e) {
            logger.debug("Falha ao carregar via classpath: {}", e.getMessage());
        }
        
        // Segunda tentativa: múltiplos caminhos de arquivo
        if (!loaded) {
            for (String path : CONFIG_PATHS) {
                try (FileInputStream fis = new FileInputStream(path)) {
                    properties.load(fis);
                    logger.info("Configurações carregadas de: {}", path);
                    loaded = true;
                    break;
                } catch (IOException e) {
                    logger.debug("Não foi possível carregar de {}: {}", path, e.getMessage());
                }
            }
        }
        
        if (!loaded) {
            logger.warn("Nenhum arquivo config.properties encontrado. Usando configurações padrão.");
            loadDefaultProperties();
        }
    }

    /**
     * Carrega configurações padrão quando arquivo não é encontrado
     */
    private void loadDefaultProperties() {
        properties.setProperty("base.url", "https://selenium-playground.testingreview.com");
        properties.setProperty("browser.default", "chrome");
        properties.setProperty("browser.headless", "false");
        properties.setProperty("browser.maximize", "true");
        properties.setProperty("implicit.wait", "20");
        properties.setProperty("explicit.wait", "15");
        properties.setProperty("page.load.timeout", "60");
        properties.setProperty("screenshot.onfailure", "true");
        properties.setProperty("retry.count", "2");
        logger.info("Configurações padrão carregadas.");
    }

    /**
     * Log das configurações carregadas para debug
     */
    private void logLoadedConfiguration() {
        if (logger.isDebugEnabled()) {
            logger.debug("=== Configurações Carregadas ===");
            properties.stringPropertyNames().stream()
                .sorted()
                .forEach(key -> {
                    String value = key.toLowerCase().contains("password") ? "***" : properties.getProperty(key);
                    logger.debug("{} = {}", key, value);
                });
            logger.debug("================================");
        }
    }    /**
     * Obtém uma propriedade, priorizando system properties sobre arquivo de configuração
     */
    public String getProperty(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = properties.getProperty(key);
        }
        return value;
    }

    /**
     * Obtém uma propriedade com valor padrão
     */
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null && !value.trim().isEmpty() ? value.trim() : defaultValue;
    }

    /**
     * Obtém propriedade como inteiro com valor padrão
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            logger.warn("Valor inválido para propriedade {}: {}. Usando valor padrão: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Obtém propriedade como boolean com valor padrão
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.trim());
    }

    /**
     * Obtém propriedade como long com valor padrão
     */
    public long getLongProperty(String key, long defaultValue) {
        String value = getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            logger.warn("Valor inválido para propriedade {}: {}. Usando valor padrão: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Verifica se uma propriedade existe
     */
    public boolean hasProperty(String key) {
        return getProperty(key) != null;
    }

    // Métodos específicos para propriedades mais utilizadas
    public String getBaseUrl() {
        return getProperty("base.url");
    }

    public String getBrowser() {
        return getProperty("browser.default", "chrome").toLowerCase();
    }

    public boolean isHeadless() {
        return getBooleanProperty("browser.headless", false);
    }

    public boolean isMaximized() {
        return getBooleanProperty("browser.maximize", true);
    }

    public int getImplicitWait() {
        return getIntProperty("implicit.wait", 20);
    }

    public int getExplicitWait() {
        return getIntProperty("explicit.wait", 15);
    }

    public int getPageLoadTimeout() {
        return getIntProperty("page.load.timeout", 60);
    }

    public boolean isScreenshotOnFailure() {
        return getBooleanProperty("screenshot.onfailure", true);
    }

    public int getRetryCount() {
        return getIntProperty("retry.count", 2);
    }

    public String getRemoteUrl() {
        return getProperty("remote.url");
    }

    public boolean isRemoteExecution() {
        String remoteUrl = getRemoteUrl();
        return remoteUrl != null && !remoteUrl.trim().isEmpty();
    }

    public String getEnvironment() {
        return getProperty("test.environment", "dev");
    }

    public int getPoolSize() {
        return getIntProperty("thread.pool.size", 5);
    }

    public String getDownloadPath() {
        return getProperty("download.path", System.getProperty("user.dir") + "/downloads");
    }

    public String getReportPath() {
        return getProperty("report.path", System.getProperty("user.dir") + "/test-output");
    }

    public String getScreenshotPath() {
        return getProperty("screenshot.path", getReportPath() + "/screenshots");
    }

    // Métodos estáticos para acesso direto - mantidos para compatibilidade
    public static String staticGetBrowser() {
        return getInstance().getBrowser();
    }

    public static boolean staticIsHeadless() {
        return getInstance().isHeadless();
    }

    public static String staticGetBaseUrl() {
        return getInstance().getBaseUrl();
    }

    public static boolean staticIsMaximized() {
        return getInstance().isMaximized();
    }

    public static int staticGetImplicitWait() {
        return getInstance().getImplicitWait();
    }

    public static int staticGetExplicitWait() {
        return getInstance().getExplicitWait();
    }

    public static int staticGetPageLoadTimeout() {
        return getInstance().getPageLoadTimeout();
    }

    /**
     * Recarrega as configurações
     */
    public void reload() {
        properties.clear();
        loadProperties();
        logLoadedConfiguration();
        logger.info("Configurações recarregadas.");
    }

    /**
     * Obtém todas as propriedades como Properties (somente leitura)
     */
    public Properties getAllProperties() {
        Properties copy = new Properties();
        copy.putAll(properties);
        return copy;
    }

    /**
     * Override toString para debug
     */
    @Override
    public String toString() {
        return "ConfigManager{propertyCount=" + properties.size() + "}";
    }
}
