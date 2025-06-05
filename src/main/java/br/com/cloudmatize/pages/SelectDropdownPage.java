package br.com.cloudmatize.pages;

import br.com.cloudmatize.config.ConfigManager;
import br.com.cloudmatize.utils.WebElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object para a página Select Dropdown Demo
 * Contém métodos para interagir com dropdowns simples e múltiplos
 */
public class SelectDropdownPage extends BasePage {    
    private static final String SELECT_DROPDOWN_URL_PART = "select-dropdown-demo";
    private final ConfigManager config = ConfigManager.getInstance();
    
    // Localizadores principais e robustos
    private final By singleSelectDropdown = By.id("select-demo");
    private final By multiSelectDropdown = By.id("multi-select");
    private final By firstSelectedButton = By.id("printMe");
    private final By getAllSelectedButton = By.id("printAll");
    private final By singleSelectResult = By.className("selected-value");
    private final By multiSelectResult = By.className("getall-selected");
    
    // Campo de resultado dos botões First Selected e Get Last Selected
    // O resultado dos botões aparece em <span class="genderbutton"> (First Selected) e <span class="groupradiobutton"> (Get Last Selected)
    private final By resultadoFirstSelected = By.cssSelector("span.genderbutton");
    private final By resultadoGetLastSelected = By.cssSelector("span.groupradiobutton");

    public SelectDropdownPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navega para a página Select Dropdown Demo
     */
    public void navegarParaPaginaSelectDropdown() {
        try {
            String url = config.getBaseUrl() + SELECT_DROPDOWN_URL_PART;
            driver.get(url);
            logger.info("Navegando para Select Dropdown Demo: {}", url);
            
            if (!validarPaginaCarregada()) {
                throw new RuntimeException("Página Select Dropdown não carregou corretamente");
            }
            
            logger.info("Página Select Dropdown carregada com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao navegar para página Select Dropdown: {}", e.getMessage(), e);
            throw new RuntimeException("Falha na navegação para página Select Dropdown", e);
        }
    }    // =============================================================================
    // MÉTODOS PARA DROPDOWN ÚNICO (SINGLE SELECT)
    // =============================================================================
    
    /**
     * Seleciona um dia da semana no dropdown único
     */
    public void selecionarDiaDaSemana(String dia) {
        try {
            logger.info("Selecionando dia da semana: '{}'", dia);
            selecionarDropdownPorTexto(singleSelectDropdown, dia);
        } catch (Exception e) {
            logger.error("Erro ao selecionar dia da semana '{}': {}", dia, e.getMessage(), e);
            throw new RuntimeException("Falha ao selecionar dia da semana: " + dia, e);
        }
    }

    /**
     * Obtém todas as opções do dropdown único
     */
    public List<WebElement> obterOpcoesDropdownUnico() {
        try {
            List<WebElement> opcoes = WebElementUtils.obterOpcoesDropdown(driver, singleSelectDropdown);
            logger.debug("Dropdown único possui {} opções", opcoes.size());
            return opcoes;
        } catch (Exception e) {
            logger.error("Erro ao obter opções do dropdown único: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao obter opções do dropdown único", e);
        }
    }    /**
     * Obtém a opção atualmente selecionada no dropdown único
     */
    public String obterOpcaoSelecionadaDropdownUnico() {
        try {
            String opcaoSelecionada = obterOpcaoSelecionadaDropdown(singleSelectDropdown);
            logger.debug("Opção selecionada no dropdown único: '{}'", opcaoSelecionada);
            return opcaoSelecionada;
        } catch (Exception e) {
            logger.error("Erro ao obter opção selecionada: {}", e.getMessage());
            return "Nenhuma opção selecionada";
        }
    }/**
     * Obtém o resultado da seleção única exibido na página
     */
    public String obterResultadoSelecaoUnica() {
        try {
            String resultado = "";
            
            // Tenta encontrar o resultado em diferentes localizadores possíveis
            if (elementoEstaVisivel(singleSelectResult)) {
                resultado = obterTextoSeguro(singleSelectResult);
            }
            
            logger.info("Resultado da seleção única: '{}'", resultado);
            return resultado;
        } catch (Exception e) {
            logger.error("Erro ao obter resultado da seleção única: {}", e.getMessage());
            return "";
        }
    }    // =============================================================================
    // MÉTODOS PARA MULTI SELECT
    // =============================================================================
    
    /**
     * Seleciona um estado no multi-select
     */
    public void selecionarEstadoMultiSelect(String estado) {
        try {
            logger.info("Selecionando estado no multi-select: '{}'", estado);
            selecionarDropdownPorTexto(multiSelectDropdown, estado);
        } catch (Exception e) {
            logger.error("Erro ao selecionar estado '{}': {}", estado, e.getMessage(), e);
            throw new RuntimeException("Falha ao selecionar estado: " + estado, e);
        }
    }

    /**
     * Seleciona múltiplos estados de uma vez
     */
    public void selecionarMultiplosEstados(String... estados) {
        try {
            for (String estado : estados) {
                selecionarEstadoMultiSelect(estado);
            }
            logger.info("Selecionados múltiplos estados: {}", String.join(", ", estados));
        } catch (Exception e) {
            logger.error("Erro ao selecionar múltiplos estados: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao selecionar múltiplos estados", e);
        }
    }

    /**
     * Obtém todas as opções do multi-select
     */
    public List<String> obterTodasOpcoesMultiSelect() {
        try {
            List<WebElement> opcoes = WebElementUtils.obterOpcoesDropdown(driver, multiSelectDropdown);
            List<String> textosOpcoes = opcoes.stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());
            
            logger.debug("Multi-select possui {} opções: {}", 
                        textosOpcoes.size(), String.join(", ", textosOpcoes));
            
            return textosOpcoes;
        } catch (Exception e) {
            logger.error("Erro ao obter opções do multi-select: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao obter opções do multi-select", e);
        }
    }

    /**
     * Limpa todas as seleções do multi-select
     */
    public void limparSelecaoMultiSelect() {
        try {
            List<WebElement> opcoes = WebElementUtils.obterOpcoesDropdown(driver, multiSelectDropdown);
            opcoes.stream()
                    .filter(WebElement::isSelected)
                    .forEach(opcao -> {
                        try {
                            opcao.click();
                        } catch (Exception e) {
                            logger.warn("Erro ao desmarcar opção: {}", e.getMessage());
                        }
                    });
            
            logger.info("Todas as seleções do multi-select foram limpas");
        } catch (Exception e) {
            logger.error("Erro ao limpar seleções do multi-select: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao limpar seleções do multi-select", e);
        }
    }    // =============================================================================
    // MÉTODOS DOS BOTÕES DE AÇÃO
    // =============================================================================
    
    /**
     * Clica no botão "First Selected"
     */
    public void clicarEmFirstSelected() {
        try {
            logger.info("Clicando no botão 'First Selected'");
            if (elementoEstaVisivel(firstSelectedButton)) {
                clicarSeguro(firstSelectedButton);
            } else {
                throw new RuntimeException("Botão 'First Selected' não encontrado");
            }
        } catch (Exception e) {
            logger.error("Erro ao clicar em 'First Selected': {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao clicar no botão First Selected", e);
        }
    }

    /**
     * Clica no botão "Get All Selected"
     */
    public void clicarEmGetLastSelected() {
        try {
            logger.info("Clicando no botão 'Get All Selected'");
            if (elementoEstaVisivel(getAllSelectedButton)) {
                clicarSeguro(getAllSelectedButton);
            } else {
                throw new RuntimeException("Botão 'Get All Selected' não encontrado");
            }
        } catch (Exception e) {
            logger.error("Erro ao clicar em 'Get All Selected': {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao clicar no botão Get All Selected", e);
        }
    }/**
     * Obtém o resultado do multi-select exibido na página
     */
    public String obterResultadoMultiSelect() {
        try {
            String resultado = "";
            if (elementoEstaVisivel(multiSelectResult)) {
                resultado = obterTextoSeguro(multiSelectResult);
            }
            
            logger.info("Resultado do multi-select: '{}'", resultado);
            return resultado;
        } catch (Exception e) {
            logger.error("Erro ao obter resultado do multi-select: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Obtém o texto exibido no campo de resultado dos botões First Selected e Get Last Selected
     */
    public String obterResultadoBotoesMultiSelect() {
        try {
            // Tenta obter o texto do resultado do First Selected
            if (elementoEstaVisivel(resultadoFirstSelected)) {
                String texto = obterTextoSeguro(resultadoFirstSelected);
                if (texto != null && !texto.trim().isEmpty()) {
                    return texto.trim();
                }
            }
            // Tenta obter o texto do resultado do Get Last Selected
            if (elementoEstaVisivel(resultadoGetLastSelected)) {
                String texto = obterTextoSeguro(resultadoGetLastSelected);
                if (texto != null && !texto.trim().isEmpty()) {
                    return texto.trim();
                }
            }
            return "";
        } catch (Exception e) {
            logger.error("Erro ao obter resultado dos botões multi-select: {}", e.getMessage());
            return "";
        }
    }

    // =============================================================================
    // MÉTODOS DE VALIDAÇÃO E VERIFICAÇÃO
    // =============================================================================

    /**
     * Verifica se uma opção específica está disponível no dropdown único
     */
    public boolean opcaoEstaDisponivel(String opcao) {
        try {
            List<WebElement> opcoes = WebElementUtils.obterOpcoesDropdown(driver, singleSelectDropdown);
            boolean disponivel = opcoes.stream()
                    .anyMatch(option -> option.getText().equals(opcao));
            
            logger.debug("Opção '{}' disponível no dropdown: {}", opcao, disponivel);
            return disponivel;
        } catch (Exception e) {
            logger.error("Erro ao verificar disponibilidade da opção '{}': {}", opcao, e.getMessage());
            return false;
        }
    }

    /**
     * Executa teste completo de multi-seleção
     */
    public void executarTesteMultiSelecao(String estado) {
        try {
            selecionarEstadoMultiSelect(estado);
            clicarEmFirstSelected();
        } catch (Exception e) {
            logger.error("Erro durante teste de multi-seleção: {}", e.getMessage(), e);
            throw new RuntimeException("Falha no teste de multi-seleção", e);
        }
    }

    /**
     * Verifica se está na URL correta da página
     */
    public boolean estaEmPaginaSelectDropdown() {
        return urlAtualContem(SELECT_DROPDOWN_URL_PART);
    }

    /**
     * Verifica se os dropdowns estão visíveis e funcionais
     */
    public boolean dropdownsEstaoFuncionais() {        try {
            boolean singleVisivel = elementoEstaVisivel(singleSelectDropdown);
            boolean multiVisivel = elementoEstaVisivel(multiSelectDropdown);
            boolean botoesPresentes = elementoEstaPresente(firstSelectedButton) && elementoEstaPresente(getAllSelectedButton);
            logger.debug("Dropdowns funcionais - Single: {}, Multi: {}, Botões: {}", singleVisivel, multiVisivel, botoesPresentes);
            return singleVisivel && multiVisivel && botoesPresentes;
        } catch (Exception e) {
            logger.error("Erro ao verificar funcionalidade dos dropdowns: {}", e.getMessage());
            return false;
        }
    }

    // =============================================================================
    // IMPLEMENTAÇÃO DO MÉTODO ABSTRATO
    // =============================================================================

    /**
     * Implementação do método abstrato da BasePage
     * Valida se a página carregou corretamente verificando elementos essenciais
     */    @Override
    protected boolean validarPaginaCarregada() {
        try {
            // Aguarda elementos essenciais estarem presentes
            boolean singleDropdownPresente = elementoEstaPresente(singleSelectDropdown);
            boolean multiDropdownPresente = elementoEstaPresente(multiSelectDropdown);
            boolean botoesPresentes = elementoEstaPresente(firstSelectedButton) && elementoEstaPresente(getAllSelectedButton);
            boolean paginaValida = singleDropdownPresente && multiDropdownPresente && botoesPresentes;
            if (paginaValida) {
                logger.debug("Página Select Dropdown validada com sucesso");
            } else {
                logger.warn("Falha na validação da página Select Dropdown - elementos ausentes");
                logger.debug("Single dropdown: {}, Multi dropdown: {}, Botões: {}", singleDropdownPresente, multiDropdownPresente, botoesPresentes);
            }
            return paginaValida;
            
        } catch (Exception e) {
            logger.error("Erro durante validação da página Select Dropdown: {}", e.getMessage());
            return false;
        }
    }

    // =============================================================================
    // MÉTODOS UTILITÁRIOS ROBUSTOS
    // =============================================================================
    
    // ...existing code...
}
