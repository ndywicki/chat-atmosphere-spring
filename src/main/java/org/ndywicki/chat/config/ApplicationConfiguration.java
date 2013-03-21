package org.ndywicki.chat.config;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "org.ndywicki"})
public class ApplicationConfiguration {

    private final Log log = LogFactory.getLog(ApplicationConfiguration.class);

    @PostConstruct
    public void initChat() throws IOException {
        log.info("Websocket-chat started!");
        
    }
}