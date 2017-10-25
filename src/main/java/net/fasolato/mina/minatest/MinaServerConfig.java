package net.fasolato.mina.minatest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
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
    @Value("${nio.charset}")
    private String nioCharset;
    @Value("${nio.log.traffic}")
    private boolean nioLogTraffic;

    private IoAcceptor server;

    @PreDestroy
    public void cleanUp() throws Exception {
        log.info("Closing application. Stopping server");
        if (server != null) {
            stopServer();
        }
    }

    public void startServer() {
        log.info(String.format("Initializing ioAcceptor on port %s", port));
        server = new NioSocketAcceptor();

        DefaultIoFilterChainBuilder fcb = new DefaultIoFilterChainBuilder();
        if (nioLogTraffic) {
            fcb.addLast("logger", new LoggingFilter());
        }
        fcb.addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName(nioCharset))));
        server.setFilterChainBuilder(fcb);

        server.setHandler(new TimeServerHandler());

        server.getSessionConfig().setReadBufferSize(readBufferSize);
        server.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, nioIdleTime);

        try {
            server.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            log.error("Error initializing bean:", e);
        }
    }

    public void stopServer() {
        if (server != null) {
            server.unbind();
        }
    }

    public String serverStatus() {
        String toReturn;

        toReturn = String.format("Server status is active: %s", server.isActive());

        return toReturn;
    }
}
