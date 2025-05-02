package lk.geoedge.interoperability.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import lk.geoedge.interoperability.IntegrationTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@IntegrationTest
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
