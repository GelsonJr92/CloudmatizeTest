package br.com.cloudmatize.steps;

import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;
import br.com.cloudmatize.pages.SelectDropdownPage;
import br.com.cloudmatize.driver.DriverFactory;
import br.com.cloudmatize.reports.ExtentTestManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import br.com.cloudmatize.utils.WebElementUtils;

/**
 * Steps para testes de dropdown e multi-select
 * Contém ações e validações para seleções em elementos dropdown
 */
public class SelectDropdownSteps {
    private static final Logger logger = LogManager.getLogger(SelectDropdownSteps.class);
    
    private SelectDropdownPage getSelectDropdownPage() {
        return new SelectDropdownPage(DriverFactory.getDriver());
    }    

    /**
     * Método genérico para encontrar e clicar em botões de dropdown pelo texto
     */
    private void clicarEmBotaoDropdownPorTexto(String textoBotao) {
        try {
            var botao = WebElementUtils.encontrarBotaoPorTexto(DriverFactory.getDriver(), textoBotao);
            botao.click();
            logger.debug("Botão de dropdown '{}' encontrado e clicado", textoBotao);
        } catch (Exception e) {
            logger.error("Erro ao clicar no botão de dropdown '{}': {}", textoBotao, e.getMessage(), e);
            throw new RuntimeException("Falha ao clicar no botão de dropdown: " + textoBotao, e);
        }
    }
    
    @Quando("eu seleciono {string} no multi-select de estados")
    public void euSelecionoNoMultiSelectDeEstados(String estado) {
        logger.info("Selecionando estado '{}' no multi-select", estado);
        getSelectDropdownPage().executarTesteMultiSelecao(estado);
        ExtentTestManager.logStep("Estado selecionado no multi-select: " + estado);
    }

    @Quando("seleciono {string} no multi-select de estados")
    public void selecionoNoMultiSelectDeEstados(String estado) {
        logger.info("Selecionando estado '{}' no multi-select", estado);
        getSelectDropdownPage().executarTesteMultiSelecao(estado);
        ExtentTestManager.logStep("Estado selecionado no multi-select: " + estado);
    }

    @Quando("eu clico no botao de dropdown {string}")
    public void euClicoNoBotaoDeDropdown(String botao) {
        logger.info("Clicando no botão de dropdown: '{}'", botao);
        
        try {
            // Usa o método genérico primeiro, depois fallback para lógica específica
            clicarEmBotaoDropdownPorTexto(botao);
            ExtentTestManager.logStep("Clicado no botão de dropdown: " + botao);
        } catch (Exception e) {
            // Fallback para lógica específica legacy
            try {
                if (botao.equals("First Selected")) {
                    getSelectDropdownPage().clicarEmFirstSelected();
                } else if (botao.equals("Get Last Selected")) {
                    getSelectDropdownPage().clicarEmGetLastSelected();
                } else {
                    throw e; // Re-lança a exceção original
                }
                ExtentTestManager.logStep("Clicado no botão de dropdown (fallback): " + botao);
            } catch (Exception fallbackError) {
                ExtentTestManager.logFail("Falha ao clicar no botão de dropdown: " + botao);
                logger.error("Falha ao clicar no botão de dropdown '{}' mesmo com fallback", botao);
                throw fallbackError;
            }
        }
    }

    @Entao("devo ver {string} no resultado do multi-select")
    public void devoVerNoResultadoDoMultiSelect(String resultadoEsperado) {
        validarResultadoDropdown("First Selected", resultadoEsperado);
    }

    @Quando("eu seleciono {string} no dropdown de dias da semana")
    public void euSelecionoNoDropdownDeDiasDaSemana(String dia) {
        logger.info("Selecionando dia da semana: '{}'", dia);
        getSelectDropdownPage().selecionarDiaDaSemana(dia);
        ExtentTestManager.logStep("Dia da semana selecionado: " + dia);
    }

    @Entao("devo ver {string} como resultado da selecao")
    public void devoVerComoResultadoDaSelecao(String resultadoEsperado) {
        logger.info("Validando resultado da seleção única: '{}'", resultadoEsperado);
        String resultado = getSelectDropdownPage().obterResultadoSelecaoUnica();
        logger.debug("Resultado da seleção única obtido: '{}'", resultado);
        
        try {
            Assert.assertEquals("Resultado da seleção não confere", resultadoEsperado, resultado);
            ExtentTestManager.logStep("✓ Resultado da seleção única validado com sucesso: " + resultadoEsperado);
        } catch (AssertionError e) {
            ExtentTestManager.logStep("✗ Falha na validação da seleção única. Esperado: '" + resultadoEsperado + "', Obtido: '" + resultado + "'");
            throw e;
        }
    }

    @Entao("o resultado deve conter {string}")
    public void oResultadoDeveConter(String textoEsperado) {
        validarResultadoDropdown("Get Last Selected", textoEsperado);
    }

    private void validarResultadoDropdown(String botao, String textoEsperado) {
        logger.info("Validando resultado do dropdown para botão '{}', deve conter: '{}'", botao, textoEsperado);
        String resultado;
        if (botao.equalsIgnoreCase("First Selected")) {
            resultado = getSelectDropdownPage().obterResultadoBotoesMultiSelect();
        } else if (botao.equalsIgnoreCase("Get Last Selected")) {
            resultado = getSelectDropdownPage().obterResultadoBotoesMultiSelect();
        } else if (botao.equalsIgnoreCase("Get All Selected")) {
            resultado = getSelectDropdownPage().obterResultadoMultiSelect();
        } else {
            resultado = getSelectDropdownPage().obterResultadoMultiSelect();
        }
        logger.debug("Resultado obtido para validação: '{}'", resultado);
        try {
            Assert.assertTrue("O resultado deveria conter: " + textoEsperado, resultado.contains(textoEsperado));
            ExtentTestManager.logStep("✓ Validação de conteúdo confirmada. Resultado contém: " + textoEsperado);
        } catch (AssertionError e) {
            ExtentTestManager.logStep("✗ Falha na validação de conteúdo. Esperado que contivesse: '" + textoEsperado + "', Obtido: '" + resultado + "'");
            throw e;
        }
    }

    @Entao("a opcao {string} deve estar selecionada no dropdown")
    public void aOpcaoDeveEstarSelecionadaNoDropdown(String opcaoEsperada) {
        logger.info("Validando que a opção '{}' está selecionada no dropdown", opcaoEsperada);
        String opcaoSelecionada = getSelectDropdownPage().obterOpcaoSelecionadaDropdownUnico();
        logger.debug("Opção selecionada no dropdown: '{}'", opcaoSelecionada);
        
        try {
            Assert.assertEquals("Opção selecionada no dropdown não confere", opcaoEsperada, opcaoSelecionada);
            ExtentTestManager.logStep("✓ Opção selecionada no dropdown validada com sucesso: " + opcaoEsperada);
        } catch (AssertionError e) {
            ExtentTestManager.logStep("✗ Falha na validação da opção selecionada. Esperado: '" + opcaoEsperada + "', Obtido: '" + opcaoSelecionada + "'");
            throw e;
        }
    }
}
