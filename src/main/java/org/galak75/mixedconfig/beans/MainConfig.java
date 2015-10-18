package org.galak75.mixedconfig.beans;

import org.galak75.mixedconfig.beans.model.MainObject;
import org.galak75.mixedconfig.beans.model.MiddleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@Import(LeafObjectsConfig.class)
@ImportResource("classpath:org/galak75/mixedconfig/beans/beans-in-the-middle.xml")
public class MainConfig {

    @Autowired
    @Qualifier("middle")
    private MiddleObject middleObject;

    @Bean
    public MainObject mainObject() {
        MainObject mainObject = new MainObject();
        mainObject.setMiddle(middleObject);
        return mainObject;
    }
}
