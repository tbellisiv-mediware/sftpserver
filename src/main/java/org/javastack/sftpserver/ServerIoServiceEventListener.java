package org.javastack.sftpserver;

import org.apache.sshd.common.io.IoAcceptor;
import org.apache.sshd.common.io.IoServiceEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ServerIoServiceEventListener implements IoServiceEventListener
{
    private static final String PROPS_EVENT_SFTP_IO_CONNECTION_DELAY_ENABLED = "sftpserver.global.event.io.connection.delay.enabled";
    private static final String PROPS_EVENT_SFTP_IO_CONNECTION_DELAY_MSECS = "sftpserver.global.event.io.connection.delay.msecs";

    private Properties props;

    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    ServerIoServiceEventListener(Properties properties)
    {
        super();
        this.props = properties;
        logEventConfig();
    }

    private void logEventConfig()
    {
        String logHeader = "[SFTP-Event-IO-Config] ";

        LOG.info(String.format("%s%s = %s", logHeader, PROPS_EVENT_SFTP_IO_CONNECTION_DELAY_ENABLED, props.getProperty(PROPS_EVENT_SFTP_IO_CONNECTION_DELAY_ENABLED)));
        LOG.info(String.format("%s%s = %s", logHeader, PROPS_EVENT_SFTP_IO_CONNECTION_DELAY_MSECS, props.getProperty(PROPS_EVENT_SFTP_IO_CONNECTION_DELAY_MSECS)));
    }

    @Override
    public void connectionAccepted(IoAcceptor acceptor, SocketAddress local, SocketAddress remote, SocketAddress service)
    {
        String logHeader = "[Event-IO-connectionAccepted] ";

        boolean delayEnabled = Boolean.parseBoolean(props.getProperty(PROPS_EVENT_SFTP_IO_CONNECTION_DELAY_ENABLED, "false"));
        int delayMsecs = Integer.parseInt(props.getProperty(PROPS_EVENT_SFTP_IO_CONNECTION_DELAY_MSECS, "0"));

        if (delayEnabled && delayMsecs > 0)
        {
            try
            {
                LOG.info(logHeader + "Delaying " + delayMsecs + " milliseconds ....");
                Thread.sleep(delayMsecs);
                LOG.info(logHeader + "Delay done");
            }
            catch (InterruptedException ex)
            {
                //gulp ...
            }
        }
    }

}
