package gestao.pessoal.jpa.aplicacao;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
// Aqui é onde estava o erro! Agora apontando para TODO o projeto.
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "gestao.pessoal")
@SelectClasspathResource("features")
public class CucumberRunnerTest {
}
