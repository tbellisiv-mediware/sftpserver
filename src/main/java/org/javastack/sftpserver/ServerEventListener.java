package org.javastack.sftpserver;

import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.subsystem.sftp.AbstractSftpEventListenerAdapter;
import org.apache.sshd.server.subsystem.sftp.FileHandle;
import org.apache.sshd.server.subsystem.sftp.Handle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ServerEventListener extends AbstractSftpEventListenerAdapter
{
    private static final String PROPS_EVENT_OPEN_DELAY_ENABLED = "sftpserver.global.event.open.delay.enabled";
    private static final String PROPS_EVENT_OPEN_DELAY_MSECS = "sftpserver.global.event.open.delay.msecs";

    private Properties props;

    public ServerEventListener(Properties properties)
    {
        super();
        this.props = properties;
        logEventConfig();
    }

    private void logEventConfig()
    {
        String logHeader = "[SFTP-Event-Config] ";

        log.info(String.format("%s%s = %s", logHeader, PROPS_EVENT_OPEN_DELAY_ENABLED, props.getProperty(PROPS_EVENT_OPEN_DELAY_ENABLED)));
        log.info(String.format("%s%s = %s", logHeader, PROPS_EVENT_OPEN_DELAY_MSECS, props.getProperty(PROPS_EVENT_OPEN_DELAY_MSECS)));
    }

    public void open(ServerSession session, String remoteHandle, Handle localHandle)
    {
        String logHeader = "[SFTP-Event-File-Opened] ";

        boolean delayEnabled = Boolean.parseBoolean(props.getProperty(PROPS_EVENT_OPEN_DELAY_ENABLED, "false"));
        int delayMsecs = Integer.parseInt(props.getProperty(PROPS_EVENT_OPEN_DELAY_MSECS, "0"));

        if (delayEnabled && delayMsecs > 0)
        {
            Path path = localHandle.getFile();
            if (Files.isRegularFile(path))
            {
                log.info(String.format(logHeader + "Opened file %s", path));

                try
                {
                    log.info(logHeader + "Delaying " + delayMsecs + " milliseconds ....");
                    Thread.sleep(delayMsecs);
                    log.info(logHeader + "Delay done");
                }
                catch (InterruptedException ex)
                {
                    //gulp ...
                }
            }
        }
    }

    public void reading(ServerSession session, String remoteHandle, FileHandle localHandle, long offset, byte[] data, int dataOffset, int dataLen) throws IOException
    {
        Path path = localHandle.getFile();
        if (Files.isRegularFile(path))
        {
            log.info(String.format("Reading file %s", path));
        }
    }

    public void read(ServerSession session, String remoteHandle, FileHandle localHandle, long offset, byte[] data, int dataOffset, int dataLen, int readLen, Throwable thrown) throws IOException
    {
        Path path = localHandle.getFile();
        if (Files.isRegularFile(path))
        {
            log.info(String.format("Read file %s", path));
        }
    }
}
