package app.spring;

import org.polyglotted.webapp.launcher.Starter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfiguration {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Starter starter() {
        return new Starter();
    }
}
