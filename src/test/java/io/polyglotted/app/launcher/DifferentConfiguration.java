package io.polyglotted.app.launcher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DifferentConfiguration {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Starter starter() {
        return new Starter();
    }
}
