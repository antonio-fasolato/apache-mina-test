package net.fasolato.mina.minatest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.Date;

public class TimeServerHandler extends IoHandlerAdapter {
    private static Logger log = LogManager.getLogger(TimeServerHandler.class);

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        log.info(String.format("Message received on session %s", session.getId()));

        String str = message.toString();
        log.info(String.format("Message: >%s<", str));
        if(str.trim().equalsIgnoreCase("quit")) {
            log.info("Closing connection");
            session.closeNow();
            return;
        }

        Date date = new Date();
        session.write(date.toString());
        log.info("Message written");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        log.info(String.format("Session %s idle (count: %s)", session.getId(), session.getIdleCount(status)));
        if(session.getIdleCount(status) >= 5) {
            log.info("Session idle time limit reached. Forcing session closed");
            session.closeNow();
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        log.error("Exception caught", cause);
        session.closeNow();
    }
}
