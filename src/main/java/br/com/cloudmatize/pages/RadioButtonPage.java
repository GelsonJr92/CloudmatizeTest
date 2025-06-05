package br.com.cloudmatize.pages;

import br.com.cloudmatize.config.ConfigManager;
import br.com.cloudmatize.utils.WebElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RadioButtonPage extends BasePage {
    private static final String RADIO_BUTTON_URL_PART = "radiobutton-demo";
    private final ConfigManager config = ConfigManager.getInstance();
    
    // Elementos da página
    private final By radioMale = By.cssSelector("input[value='Male']");
    private final By radioFemale = By.cssSelector("input[value='Female']");
      // Elementos de faixa etária (seção Age Group)
    private final By ageGroup0to5 = By.xpath("//input[@name='ageGroup' and @value='0 - 5']");
    private final By ageGroup5to15 = By.xpath("//input[@name='ageGroup' and @value='5 - 15']");
    private final By ageGroup15to50 = By.xpath("//input[@name='ageGroup' and @value='15 - 50']");
    
    private final By getValuesButton = By.xpath("//button[contains(text(),'Get values')]");
    private final By resultMessage = By.cssSelector(".groupradiobutton");

    public RadioButtonPage(org.openqa.selenium.WebDriver driver) {
        super(driver);
    }    public void navegarParaPaginaRadioButtons() {
        String url = config.getBaseUrl() + RADIO_BUTTON_URL_PART;
        driver.get(url);
        logger.info("Navegando para Radio Button Demo: {}", url);
    }    /**
     * Verifica se a página está carregada
     */
    public boolean paginaEstaCarregada() {
        boolean urlCorrect = obterUrlAtual().contains("radiobutton-demo");
        boolean radioPresent = elementoEstaPresente(radioMale);
        logger.debug("Radio Button Page carregada - URL correta: {}, Radio presente: {}", urlCorrect, radioPresent);
        return urlCorrect && radioPresent;
    }

    public void selecionarGenero(String genero) {
        if (genero.equalsIgnoreCase("masculino") || genero.equalsIgnoreCase("male")) {
            clicarSeguro(radioMale);
            logger.info("Gênero masculino selecionado");
        } else if (genero.equalsIgnoreCase("feminino") || genero.equalsIgnoreCase("female")) {
            clicarSeguro(radioFemale);
            logger.info("Gênero feminino selecionado");
        } else {
            logger.warn("Gênero não reconhecido: {}", genero);
        }
    }    public void selecionarFaixaEtaria(String faixa) {
        logger.info("Selecionando faixa etária: '{}'", faixa);
        
        try {
            switch (faixa.trim()) {
                case "0 - 5":
                case "0-5":
                    clicarSeguro(ageGroup0to5);
                    logger.info("Faixa etária 0-5 selecionada");
                    break;
                case "5 - 15":
                case "5-15":
                    clicarSeguro(ageGroup5to15);
                    logger.info("Faixa etária 5-15 selecionada");
                    break;
                case "15 - 50":
                case "15-50":
                    clicarSeguro(ageGroup15to50);
                    logger.info("Faixa etária 15-50 selecionada");
                    break;
                default:
                    logger.error("Faixa etária não reconhecida: {}", faixa);
                    throw new IllegalArgumentException("Faixa etária não reconhecida: " + faixa);
            }
        } catch (Exception e) {
            logger.error("Erro ao selecionar faixa etária '{}': {}", faixa, e.getMessage());
            throw new RuntimeException("Falha ao selecionar faixa etária: " + faixa, e);
        }
    }    public void clicarEmGetValues() {
        logger.info("Clicando no botão Get values");
        try {
            // Primeiro tenta com o seletor direto
            if (elementoEstaPresente(getValuesButton)) {
                clicarSeguro(getValuesButton);
            } else {
                // Se não encontrar, usa o método genérico
                WebElement botao = WebElementUtils.encontrarBotaoPorTexto(driver, "Get values");
                if (botao != null) {
                    botao.click();
                    logger.info("Botão 'Get values' clicado usando método genérico");
                } else {
                    throw new RuntimeException("Botão 'Get values' não encontrado");
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao clicar no botão Get values: {}", e.getMessage());
            throw new RuntimeException("Falha ao clicar no botão Get values", e);
        }
    }

    public String obterMensagemResultado() {
        String result = obterTextoSeguro(resultMessage);
        logger.info("Resultado: '{}'", result);
        return result;
    }

    public boolean isRadioButtonSelecionado(String genero) {
        boolean selected = false;
        if (genero.equalsIgnoreCase("masculino") || genero.equalsIgnoreCase("male")) {
            selected = driver.findElement(radioMale).isSelected();
        } else if (genero.equalsIgnoreCase("feminino") || genero.equalsIgnoreCase("female")) {
            selected = driver.findElement(radioFemale).isSelected();
        }
        logger.debug("Radio button '{}' selecionado: {}", genero, selected);
        return selected;
    }
      /**
     * Verifica se uma faixa etária está selecionada
     */
    public boolean isFaixaEtariaSelecionada(String faixa) {
        try {
            boolean selected = false;
            switch (faixa.trim()) {
                case "0 - 5":
                case "0-5":
                    selected = driver.findElement(ageGroup0to5).isSelected();
                    break;
                case "5 - 15":
                case "5-15":
                    selected = driver.findElement(ageGroup5to15).isSelected();
                    break;
                case "15 - 50":
                case "15-50":
                    selected = driver.findElement(ageGroup15to50).isSelected();
                    break;
            }
            logger.debug("Faixa etária '{}' selecionada: {}", faixa, selected);
            return selected;
        } catch (Exception e) {
            logger.error("Erro ao verificar seleção da faixa etária '{}': {}", faixa, e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtém o gênero atualmente selecionado
     */
    public String obterGeneroSelecionado() {
        if (driver.findElement(radioMale).isSelected()) {
            return "Male";
        } else if (driver.findElement(radioFemale).isSelected()) {
            return "Female";
        }
        return "Nenhum gênero selecionado";
    }
      /**
     * Obtém a faixa etária atualmente selecionada
     */
    public String obterFaixaEtariaSelecionada() {
        try {
            if (driver.findElement(ageGroup0to5).isSelected()) {
                return "0 - 5";
            } else if (driver.findElement(ageGroup5to15).isSelected()) {
                return "5 - 15";
            } else if (driver.findElement(ageGroup15to50).isSelected()) {
                return "15 - 50";
            }
            return "Nenhuma faixa etária selecionada";
        } catch (Exception e) {
            logger.error("Erro ao obter faixa etária selecionada: {}", e.getMessage());
            return "Erro ao obter faixa etária";
        }
    }
    
    /**
     * Limpa todas as seleções
     */
    public void limparSelecoes() {
        // Radio buttons não podem ser "desmarcados" programaticamente
        // mas podemos navegar novamente para a página para reset
        logger.info("Radio buttons não podem ser limpos. Para reset, navegue novamente para a página.");
    }
      /**
     * Valida se página de radio buttons está carregada
     */
    public boolean validarPaginaCarregada() {
        return paginaEstaCarregada() && 
               elementoEstaVisivel(radioMale) && 
               elementoEstaVisivel(radioFemale) &&
               elementoEstaVisivel(ageGroup5to15) &&
               elementoEstaVisivel(getValuesButton);
    }
    
    /**
     * Executa teste completo de seleção
     */
    public void executarTesteCompleto(String genero, String faixa) {
        selecionarGenero(genero);
        selecionarFaixaEtaria(faixa);
        clicarEmGetValues();
        logger.info("Teste completo executado com gênero: {} e faixa: {}", genero, faixa);
    }
}
