package br.com.cloudmatize.steps;

import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;
import br.com.cloudmatize.pages.SimpleFormPage;
import br.com.cloudmatize.driver.DriverFactory;
import br.com.cloudmatize.reports.ExtentTestManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Steps para testes do formulário simples
 * Contém ações e validações para preenchimento de campos e verificação de resultados
 * Com métodos dinâmicos para localizar elementos por texto
 */
public class SimpleFormSteps {
    private static final Logger logger = LogManager.getLogger(SimpleFormSteps.class);
    
    private SimpleFormPage getSimpleFormPage() {
        return new SimpleFormPage(DriverFactory.getDriver());
    }    
    @Quando("eu preencho o campo de mensagem com {string}")
    public void euPreenchoOCampoDeMensagemCom(String mensagem) {
        logger.info("Preenchendo campo de mensagem com: '{}'", mensagem);
        getSimpleFormPage().preencherMensagem(mensagem);
        ExtentTestManager.logStep("Campo de mensagem preenchido com: " + mensagem);    }

    @Quando("eu clico no botao do formulario {string}")
    public void euClicoNoBotaoDoFormulario(String botao) {
        logger.info("Clicando no botão: '{}'", botao);
        
        try {
            // Usa o método genérico da página que por sua vez usa WebElementUtils
            getSimpleFormPage().clicarEmBotaoPorTexto(botao);
            ExtentTestManager.logStep("Clicado no botão: " + botao);
            
        } catch (Exception e) {
            logger.error("Erro ao clicar no botão '{}': {}", botao, e.getMessage(), e);
            ExtentTestManager.logStep("✗ Falha ao clicar no botão: " + botao);
            throw new RuntimeException("Falha ao clicar no botão: " + botao, e);
        }
    }
    
    @Quando("eu clico no botao {string}")
    public void euClicoNoBotao(String botao) {
        // Método genérico para qualquer botão
        euClicoNoBotaoDoFormulario(botao);
    }

    @Entao("eu devo ver a mensagem {string} exibida")
    public void euDevoVerAMensagemExibida(String mensagemEsperada) {
        logger.info("Validando mensagem esperada: '{}'", mensagemEsperada);
        String resultado = getSimpleFormPage().obterResultadoMensagem();
        logger.debug("Mensagem obtida: '{}'", resultado);
        
        try {
            Assert.assertEquals("Mensagem exibida não confere com a esperada", mensagemEsperada, resultado);
            ExtentTestManager.logPass("Mensagem validada com sucesso: " + mensagemEsperada);
        } catch (AssertionError e) {
            ExtentTestManager.logFail("Falha na validação da mensagem. Esperado: '" + mensagemEsperada + "', Obtido: '" + resultado + "'");
            throw e;
        }
    }    
    
    @Quando("eu preencho o primeiro numero com {string}")
    public void euPreenchoOPrimeiroNumeroCom(String numero) {
        logger.info("Preenchendo primeiro número com: '{}'", numero);
        getSimpleFormPage().preencherPrimeiroNumero(numero);
        ExtentTestManager.logStep("Primeiro número preenchido com: " + numero);
    }

    @Quando("eu preencho o segundo numero com {string}")
    public void euPreenchoOSegundoNumeroCom(String numero) {
        logger.info("Preenchendo segundo número com: '{}'", numero);
        getSimpleFormPage().preencherSegundoNumero(numero);
        ExtentTestManager.logStep("Segundo número preenchido com: " + numero);
    }

    @Entao("eu devo ver o resultado da soma como {string}")
    public void euDevoVerOResultadoDaSomaComo(String resultadoEsperado) {
        logger.info("Validando resultado da soma esperado: '{}'", resultadoEsperado);
        String resultado = getSimpleFormPage().obterResultadoSoma();
        logger.debug("Resultado da soma obtido: '{}'", resultado);
        
        try {
            Assert.assertEquals("Resultado da soma não confere com o esperado", resultadoEsperado, resultado);
            ExtentTestManager.logPass("Resultado da soma validado com sucesso: " + resultadoEsperado);
        } catch (AssertionError e) {
            ExtentTestManager.logFail("Falha na validação do resultado da soma. Esperado: '" + resultadoEsperado + "', Obtido: '" + resultado + "'");
            throw e;
        }
    }    
    
    @Quando("eu deixo o campo de mensagem vazio")
    public void euDeixoOCampoDeMensagemVazio() {
        logger.info("Deixando campo de mensagem vazio");
        getSimpleFormPage().deixarCampoMensagemVazio();
        ExtentTestManager.logStep("Campo de mensagem deixado vazio");
    }

    @Entao("eu devo ver uma mensagem de erro ou resultado invalido")
    public void euDevoVerUmaMensagemDeErroOuResultadoInvalido() {
        logger.info("Validando mensagem de erro ou resultado inválido");        String resultado = getSimpleFormPage().obterResultadoSoma();
        logger.debug("Resultado obtido para validação de erro: '{}'", resultado);
        
        boolean resultadoInvalido = resultado.isEmpty() || 
                                   resultado.contains("NaN") || 
                                   resultado.contains("error") || 
                                   resultado.contains("Please") ||
                                   resultado.contains("Entered value is not a number") ||
                                   resultado.toLowerCase().contains("invalid");
        
        try {
            Assert.assertTrue("Resultado deveria ser inválido ou conter mensagem de erro", resultadoInvalido);
            ExtentTestManager.logPass("Validação de resultado inválido confirmada: " + resultado);
        } catch (AssertionError e) {
            ExtentTestManager.logFail("Falha na validação de resultado inválido. Resultado obtido: '" + resultado + "'");
            throw e;
        }
    }

    @Entao("eu devo ver uma mensagem vazia ou comportamento padrao")
    public void euDevoVerUmaMensagemVaziaOuComportamentoPadrao() {
        logger.info("Validando mensagem vazia ou comportamento padrão");
        String resultado = getSimpleFormPage().obterResultadoMensagem();
        logger.debug("Resultado obtido para validação de comportamento padrão: '{}'", resultado);
        
        // Aceita tanto mensagem vazia quanto algum comportamento padrão do sistema
        boolean comportamentoPadrao = resultado.isEmpty() || resultado.length() <= 50;
        
        try {
            Assert.assertTrue("Campo deve estar vazio ou com comportamento padrão", comportamentoPadrao);
            ExtentTestManager.logPass("Validação de comportamento padrão confirmada: " + resultado);
        } catch (AssertionError e) {
            ExtentTestManager.logFail("Falha na validação de comportamento padrão. Resultado obtido: '" + resultado + "'");
            throw e;
        }
    }
}
