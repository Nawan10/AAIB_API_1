package lk.geoedge.interoperability;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lk.geoedge.interoperability.config.AsyncSyncConfiguration;
import lk.geoedge.interoperability.config.EmbeddedSQL;
import lk.geoedge.interoperability.config.JacksonConfiguration;
import lk.geoedge.interoperability.config.TestSecurityConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { Aaibapi1App.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
