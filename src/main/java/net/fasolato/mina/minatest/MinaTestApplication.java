package net.fasolato.mina.minatest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MinaTestApplication implements CommandLineRunner {
    private static Logger log = LogManager.getLogger(MinaTestApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MinaTestApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        try {
            log.info("Application started");
        } finally {
            log.info("Application stopped");
        }
    }
}
