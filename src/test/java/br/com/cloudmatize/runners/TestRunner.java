package br.com.cloudmatize.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Runner principal para executar todos os testes de automação
 *
 * EXECUÇÃO POR TAGS - Descomente a linha desejada:
 * - Para executar apenas uma tag específica: tags = "@tag_desejada"
 * - Para combinar tags: tags = "@tag1 and @tag2"
 * - Para tags alternativas: tags = "@tag1 or @tag2"
 * - Para excluir tags: tags = "not @tag_indesejada"
 *
 * EXEMPLOS DE USO VIA COMANDO:
 * mvn test -Dtest=TestRunner -Dcucumber.filter.tags="@dropdown"
 * mvn test -Dtest=TestRunner -Dcucumber.filter.tags="@positivo"
 * mvn test -Dtest=TestRunner -Dcucumber.filter.tags="@dropdown and @positivo"
 */
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"br.com.cloudmatize.steps", "br.com.cloudmatize.hooks"},
    tags = "",
    dryRun = false,
    plugin = {
        "pretty",
        "html:target/cucumber-reports/html",
        "json:target/cucumber-reports/Cucumber.json",
        "junit:target/cucumber-reports/Cucumber.xml",
        "timeline:target/cucumber-reports/timeline"
    },
    publish = false,
    monochrome = true
)
public class TestRunner {
}
