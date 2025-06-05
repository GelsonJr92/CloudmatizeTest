package br.com.cloudmatize.steps;

import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;
import br.com.cloudmatize.pages.RadioButtonPage;
import br.com.cloudmatize.driver.DriverFactory;
import br.com.cloudmatize.reports.ExtentTestManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Steps para testes de radio buttons
 * Contém ações e validações para seleções de radio buttons
 */
public class RadioButtonSteps {
    private static final Logger logger = LogManager.getLogger(RadioButtonSteps.class);
    
    private RadioButtonPage getRadioButtonPage() {
        return new RadioButtonPage(DriverFactory.getDriver());
    }    @Quando("eu seleciono o radio button {string}")
    public void euSelecionoORadioButton(String genero) {
        logger.info("Selecionando radio button de gênero: '{}'", genero);
        getRadioButtonPage().selecionarGenero(genero);
        ExtentTestManager.logStep("Radio button de gênero selecionado: " + genero);
    }

    @Quando("seleciono o radio button {string}")
    public void selecionoORadioButtonGenero(String genero) {
        logger.info("Selecionando radio button de gênero: '{}'", genero);
        getRadioButtonPage().selecionarGenero(genero);
        ExtentTestManager.logStep("Radio button de gênero selecionado: " + genero);
    }    @Quando("eu seleciono a faixa etaria {string}")
    public void euSelecionoAFaixaEtaria(String faixa) {
        logger.info("Selecionando faixa etária: '{}'", faixa);
        getRadioButtonPage().selecionarFaixaEtaria(faixa);
        ExtentTestManager.logStep("Faixa etária selecionada: " + faixa);
    }

    @Quando("seleciono a faixa etaria {string}")
    public void selecionoAFaixaEtaria(String faixa) {
        logger.info("Selecionando faixa etária: '{}'", faixa);
        getRadioButtonPage().selecionarFaixaEtaria(faixa);
        ExtentTestManager.logStep("Faixa etária selecionada: " + faixa);
    }

    @Quando("eu clico no botao de radio {string}")
    public void euClicoNoBotaoDeRadio(String botao) {
        logger.info("Clicando no botão de radio: '{}'", botao);
        getRadioButtonPage().clicarEmGetValues();
        ExtentTestManager.logStep("Clicado no botão: " + botao);
    }    @Entao("devo ver a mensagem completa {string}")
    public void devoVerAMensagemCompleta(String mensagemEsperada) {
        logger.info("Validando mensagem completa esperada: '{}'", mensagemEsperada);
        String resultado = getRadioButtonPage().obterMensagemResultado();
        logger.debug("Mensagem completa obtida: '{}'", resultado);
        
        try {
            Assert.assertTrue("Mensagem completa não contém o texto esperado. Resultado: '" + resultado + 
                            "' não contém: '" + mensagemEsperada + "'", 
                            resultado.contains(mensagemEsperada));
            ExtentTestManager.logStep("✓ Mensagem completa validada com sucesso - contém: " + mensagemEsperada);
        } catch (AssertionError e) {
            ExtentTestManager.logStep("✗ Falha na validação da mensagem completa. Esperada que contivesse: '" + mensagemEsperada + "', Obtida: '" + resultado + "'");
            throw e;
        }}
    
    @Quando("seleciono o radio button feminino")
    public void selecionoORadioButtonFeminino() {
        logger.info("Selecionando radio button feminino");
        getRadioButtonPage().selecionarGenero("feminino");
        ExtentTestManager.logStep("Radio button feminino selecionado");
    }

    @Quando("seleciono o radio button masculino")
    public void selecionoORadioButtonMasculino() {
        logger.info("Selecionando radio button masculino");
        getRadioButtonPage().selecionarGenero("masculino");
        ExtentTestManager.logStep("Radio button masculino selecionado");
    }

    @Entao("o radio button feminino deve estar selecionado")
    public void oRadioButtonFemininodeveEstarSelecionado() {
        logger.info("Validando se radio button feminino está selecionado");
        boolean selecionado = getRadioButtonPage().isRadioButtonSelecionado("feminino");
        logger.debug("Status do radio button feminino: {}", selecionado ? "selecionado" : "não selecionado");
        
        try {
            Assert.assertTrue("Radio button feminino deveria estar selecionado", selecionado);
            ExtentTestManager.logStep("✓ Radio button feminino está selecionado conforme esperado");
        } catch (AssertionError e) {
            ExtentTestManager.logStep("✗ Radio button feminino não está selecionado");
            throw e;
        }
    }

    @Entao("o radio button masculino deve estar desmarcado")
    public void oRadioButtonMasculinoDeveEstarDesmarcado() {
        logger.info("Validando se radio button masculino está desmarcado");
        boolean selecionado = getRadioButtonPage().isRadioButtonSelecionado("masculino");
        logger.debug("Status do radio button masculino: {}", selecionado ? "selecionado" : "não selecionado");
        
        try {
            Assert.assertFalse("Radio button masculino deveria estar desmarcado", selecionado);
            ExtentTestManager.logStep("✓ Radio button masculino está desmarcado conforme esperado");
        } catch (AssertionError e) {
            ExtentTestManager.logStep("✗ Radio button masculino está selecionado quando deveria estar desmarcado");
            throw e;
        }
    }
}
