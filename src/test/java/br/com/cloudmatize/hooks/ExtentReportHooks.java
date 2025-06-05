package br.com.cloudmatize.hooks;

import br.com.cloudmatize.reports.ExtentTestManager;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportHooks {
    private static final Logger logger = LogManager.getLogger(ExtentReportHooks.class);

    @BeforeAll
    public static void beforeAll() {
        logger.info("=== INICIANDO EXECUÇÃO DOS TESTES ===");
        logger.info("Timestamp: {}", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        
        // Criar diretório de reports se não existir
        File reportsDir = new File("target/extent-reports");
        if (!reportsDir.exists()) {
            boolean created = reportsDir.mkdirs();
            logger.info("Diretório de relatórios criado: {}", created);
        }
        
        logger.info("ExtentReports inicializado com sucesso");
    }    @AfterAll
    public static void afterAll() {
        logger.info("=== FINALIZANDO EXECUÇÃO DOS TESTES ===");
        
        try {
            // Finalizar relatório
            ExtentTestManager.flush();
            logger.info("Relatório ExtentReports gerado com sucesso");
            
            // Limpar recursos
            ExtentTestManager.removeTest();
            
        } catch (Exception e) {
            logger.error("Erro ao finalizar relatórios: {}", e.getMessage(), e);
        }
        
        logger.info("Timestamp final: {}", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        logger.info("=== EXECUÇÃO FINALIZADA ===");
    }
}
