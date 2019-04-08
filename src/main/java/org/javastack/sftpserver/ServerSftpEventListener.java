package org.javastack.sftpserver;

import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.subsystem.sftp.AbstractSftpEventListenerAdapter;
import org.apache.sshd.server.subsystem.sftp.FileHandle;
import org.apache.sshd.server.subsystem.sftp.Handle;

import java.nio.file.Path;
import java.util.Properties;

@SuppressWarnings("Duplicates")
public class ServerSftpEventListener extends AbstractSftpEventListenerAdapter
{
    private static final String PROPS_EVENT_SFTP_OPEN_DELAY_ENABLED = "sftpserver.global.event.sftp.open.delay.enabled";
    private static final String PROPS_EVENT_SFTP_OPEN_DELAY_MSECS = "sftpserver.global.event.sftp.open.delay.msecs";

    private static final String PROPS_EVENT_SFTP_OPENING_DELAY_ENABLED = "sftpserver.global.event.sftp.opening.delay.enabled";
    private static final String PROPS_EVENT_SFTP_OPENING_DELAY_MSECS = "sftpserver.global.event.sftp.opening.delay.msecs";

    private static final String PROPS_EVENT_SFTP_READING_DELAY_ENABLED = "sftpserver.global.event.sftp.reading.delay.enabled";
    private static final String PROPS_EVENT_SFTP_READING_DELAY_MSECS = "sftpserver.global.event.sftp.reading.delay.msecs";

    private static final String PROPS_EVENT_SFTP_WRITING_DELAY_ENABLED = "sftpserver.global.event.sftp.writing.delay.enabled";
    private static final String PROPS_EVENT_SFTP_WRITING_DELAY_MSECS = "sftpserver.global.event.sftp.writing.delay.msecs";

    private Properties props;

    ServerSftpEventListener(Properties properties)
    {
        super();
        this.props = properties;
        logEventConfig();
    }

    private void logEventConfig()
    {
        String logHeader = "[Event-SFTP-Config] ";

        log.info(String.format("%s%s = %s", logHeader, PROPS_EVENT_SFTP_OPEN_DELAY_ENABLED, props.getProperty(PROPS_EVENT_SFTP_OPEN_DELAY_ENABLED)));
        log.info(String.format("%s%s = %s", logHeader, PROPS_EVENT_SFTP_OPEN_DELAY_MSECS, props.getProperty(PROPS_EVENT_SFTP_OPEN_DELAY_MSECS)));

        log.info(String.format("%s%s = %s", logHeader, PROPS_EVENT_SFTP_OPENING_DELAY_ENABLED, props.getProperty(PROPS_EVENT_SFTP_OPENING_DELAY_ENABLED)));
        log.info(String.format("%s%s = %s", logHeader, PROPS_EVENT_SFTP_OPENING_DELAY_MSECS, props.getProperty(PROPS_EVENT_SFTP_OPENING_DELAY_MSECS)));

        log.info(String.format("%s%s = %s", logHeader, PROPS_EVENT_SFTP_READING_DELAY_ENABLED, props.getProperty(PROPS_EVENT_SFTP_READING_DELAY_ENABLED)));
        log.info(String.format("%s%s = %s", logHeader, PROPS_EVENT_SFTP_READING_DELAY_MSECS, props.getProperty(PROPS_EVENT_SFTP_READING_DELAY_MSECS)));

        log.info(String.format("%s%s = %s", logHeader, PROPS_EVENT_SFTP_WRITING_DELAY_ENABLED, props.getProperty(PROPS_EVENT_SFTP_WRITING_DELAY_ENABLED)));
        log.info(String.format("%s%s = %s", logHeader, PROPS_EVENT_SFTP_WRITING_DELAY_MSECS, props.getProperty(PROPS_EVENT_SFTP_WRITING_DELAY_MSECS)));
    }

    public void open(ServerSession session, String remoteHandle, Handle localHandle)
    {
        String logHeader = "[Event-SFTP-File-Open] ";

        boolean delayEnabled = Boolean.parseBoolean(props.getProperty(PROPS_EVENT_SFTP_OPEN_DELAY_ENABLED, "false"));
        int delayMsecs = Integer.parseInt(props.getProperty(PROPS_EVENT_SFTP_OPEN_DELAY_MSECS, "0"));

        if (delayEnabled && delayMsecs > 0)
        {
            Path path = localHandle.getFile();

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

    @Override
    public void opening(ServerSession session, String remoteHandle, Handle localHandle)
    {
        String logHeader = "[Event-SFTP-File-Opening] ";

        boolean delayEnabled = Boolean.parseBoolean(props.getProperty(PROPS_EVENT_SFTP_OPENING_DELAY_ENABLED, "false"));
        int delayMsecs = Integer.parseInt(props.getProperty(PROPS_EVENT_SFTP_OPENING_DELAY_MSECS, "0"));

        if (delayEnabled && delayMsecs > 0)
        {
            Path path = localHandle.getFile();

            log.info(String.format(logHeader + "Opening file %s", path));

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

    @Override
    public void reading(ServerSession session, String remoteHandle, FileHandle localHandle, long offset, byte[] data, int dataOffset, int dataLen)
    {
        String logHeader = "[Event-SFTP-File-Reading] ";

        boolean delayEnabled = Boolean.parseBoolean(props.getProperty(PROPS_EVENT_SFTP_READING_DELAY_ENABLED, "false"));
        int delayMsecs = Integer.parseInt(props.getProperty(PROPS_EVENT_SFTP_READING_DELAY_MSECS, "0"));

        if (delayEnabled && delayMsecs > 0)
        {
            Path path = localHandle.getFile();
            log.info(String.format(logHeader + "Reading file %s", path));

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

    @Override
    public void writing(ServerSession session, String remoteHandle, FileHandle localHandle, long offset, byte[] data, int dataOffset, int dataLen)
    {
        String logHeader = "[Event-SFTP-File-Writing] ";

        boolean delayEnabled = Boolean.parseBoolean(props.getProperty(PROPS_EVENT_SFTP_WRITING_DELAY_ENABLED, "false"));
        int delayMsecs = Integer.parseInt(props.getProperty(PROPS_EVENT_SFTP_WRITING_DELAY_MSECS, "0"));

        if (delayEnabled && delayMsecs > 0)
        {
            Path path = localHandle.getFile();
            log.info(String.format(logHeader + "Writing file %s", path));

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
