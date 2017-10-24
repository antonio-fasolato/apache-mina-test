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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

@Component
public class MinaServerConfig {
    private static Logger log = LogManager.getLogger(MinaServerConfig.class);

    @Value("${nio.port}")
    private int port;
    @Value("${nio.idle.time}")
    private int nioIdleTime;
    @Value("${nio.readBufferSize}")
    private int readBufferSize;

    @Autowired
    private DefaultIoFilterChainBuilder filterChainBuilder;
    @Autowired
    private TimeServerHandler timeServerHandler;

    @Bean
    public TimeServerHandler timeServerHandler() {
        return new TimeServerHandler();
    }

    @Bean
    public DefaultIoFilterChainBuilder defaultIoFilterChainBuilder() {
        DefaultIoFilterChainBuilder fcb = new DefaultIoFilterChainBuilder();
//        fcb.addLast("logger", new LoggingFilter());
        fcb.addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
        return fcb;
    }

    @Bean
    public IoAcceptor ioAcceptor() {
        log.info(String.format("Initializing ioAcceptor on port %s", port));
        IoAcceptor acceptor = new NioSocketAcceptor();

        acceptor.setFilterChainBuilder(filterChainBuilder);

        acceptor.setHandler(timeServerHandler);

        acceptor.getSessionConfig().setReadBufferSize(readBufferSize);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, nioIdleTime);

        try {
            acceptor.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            log.error("Error initializing bean:", e);
            return acceptor;
        }
        return null;
    }
}
