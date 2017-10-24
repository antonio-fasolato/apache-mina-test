package net.fasolato.mina.minatest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MinaTestApplication {
    private static Logger log = LogManager.getLogger(MinaTestApplication.class);

    @Autowired
    IoAcceptor server;

    public static void main(String[] args) {
        SpringApplication.run(MinaTestApplication.class, args);
    }
}
