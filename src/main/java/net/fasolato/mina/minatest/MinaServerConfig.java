package net.fasolato.mina.minatest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

@Component
public class MinaServerConfig {
    private static Logger log = LogManager.getLogger(MinaServerConfig.class);

    @Autowired
    private DefaultIoFilterChainBuilder filterChainBuilder;
    @Autowired
    private TimeServerHandler timeServerHandler;

    @Bean
    public TimeServerHandler timeServerHandler() {
        return new TimeServerHandler();
    }

    @Bean
    public DefaultIoFilterChainBuilder filterChain() {
        DefaultIoFilterChainBuilder fcb = new DefaultIoFilterChainBuilder();
//        fcb.addLast("logger", new LoggingFilter());
        fcb.addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
        return fcb;
    }

    @Bean
    public IoAcceptor ioAcceptor() {
        log.info("Initializing ioAcceptor");
        IoAcceptor acceptor = new NioSocketAcceptor();

        acceptor.setFilterChainBuilder(filterChainBuilder);

        acceptor.setHandler(timeServerHandler);

        acceptor.getSessionConfig().setReadBufferSize(2028);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

        try {
            acceptor.bind(new InetSocketAddress(9080));
        } catch (IOException e) {
            log.error("Error initializing bean:", e);
            return acceptor;
        }
        return null;
    }
}
