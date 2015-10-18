package org.galak75.mixedconfig.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LeafObjectsConfig {

    @Bean
    public String leaf() {
        return new String("Inner String");
    }
}
