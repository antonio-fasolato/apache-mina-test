package net.fasolato.mina.minatest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

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

            IoAcceptor acceptor = new NioSocketAcceptor();

//            acceptor.getFilterChain().addLast("logger", new LoggingFilter());
            acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

            acceptor.setHandler(new TimeServerHandler());

            acceptor.getSessionConfig().setReadBufferSize(2028);
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

            acceptor.bind(new InetSocketAddress(9080));
        } finally {
            log.info("Application stopped");
        }
    }
}
