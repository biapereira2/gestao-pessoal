package gestao.pessoal.aplicacao;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
// 1. ONDE ESTÃO OS FEATURES? O valor 'features' aponta para src/test/resources/features
@SelectClasspathResource("features")
// 2. ONDE ESTÃO OS STEPS? O valor 'gestao.pessoal.aplicacao' aponta para o pacote Java com os Steps.
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "gestao.pessoal.aplicacao")
public class CucumberRunnerTest {
}
