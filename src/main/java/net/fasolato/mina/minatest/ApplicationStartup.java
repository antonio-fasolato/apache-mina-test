package net.fasolato.mina.minatest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
    private static Logger log = LogManager.getLogger(ApplicationStartup.class);

    @Autowired
    MinaServerConfig minaServerConfig;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("Appplication started. Initializing Apache MINA");

        minaServerConfig.startServer();
    }
}
