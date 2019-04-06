package org.javastack.sftpserver;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.config.hosts.HostConfigEntryResolver;
import org.apache.sshd.client.keyverifier.AcceptAllServerKeyVerifier;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.client.simple.SimpleClient;
import org.apache.sshd.client.subsystem.sftp.SftpClient;
import org.apache.sshd.client.subsystem.sftp.SimpleSftpClient;
import org.apache.sshd.client.subsystem.sftp.impl.SimpleSftpClientImpl;
import org.apache.sshd.common.keyprovider.KeyIdentityProvider;
import org.junit.After;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class TestServer
{
    private Server server;
    private SshClient client;
    private SimpleClient simple;
    private SimpleSftpClient sftpClient;

    private static final Logger log = LoggerFactory.getLogger(Server.class);

    private static final List<String> TARGET_FOLDER_NAMES =    // NOTE: order is important
            Collections.unmodifiableList(
                    Arrays.asList(
                            "target" /* Maven */,
                            "build" /* Gradle */));

    private static final String TEMP_SUBFOLDER_NAME = "temp";

    @Rule
    public final RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

    @Before
    public void setUp() throws IOException
    {
        System.setProperty("sftp.home", "target/test/home");

        // Init server
        server = new Server();

        Properties serverProps = server.getConfig();
        log.info("SFTP Server Properties:");
        serverProps.forEach((k,v) -> log.info(String.format("  %s = %s", k, v)));

        server.start(false);

        // Init client
        client = SshClient.setUpDefaultClient();
        client.setServerKeyVerifier(AcceptAllServerKeyVerifier.INSTANCE);
        client.setHostConfigEntryResolver(HostConfigEntryResolver.EMPTY);
        client.setKeyIdentityProvider(KeyIdentityProvider.EMPTY_KEYS_PROVIDER);
        simple = SshClient.wrapAsSimpleClient(client);
        simple.setConnectTimeout(20 * 1000);
        simple.setAuthenticationTimeout(10 * 1000);
        client.start();
        sftpClient = new SimpleSftpClientImpl(simple);

        // Clean existing files from previous runs
        cleanFiles();
    }


    private SftpClient login() throws IOException
    {
        return sftpClient.sftpLogin("localhost", 22222, "test", "test");
    }

    @After
    public void tearDown() throws Exception
    {
        log.info("Shutting down client");

        if (simple != null)
        {
            simple.close();
        }

        if (client != null)
        {
            client.stop();
        }

        log.info("Shutting down server");

        if (server != null)
        {
            server.stop();
        }

        // Clean existing files from previous runs
        cleanFiles();
    }

    @Test
    public void testLogin() throws IOException
    {
        SftpClient sftp = login();

        ClientSession session = sftp.getSession();

        assertTrue(session.isOpen());
        assertTrue(session.isAuthenticated());
    }


    private void cleanFiles()
    {
        File uploaded = new File("target/uploaded.txt");
        if (uploaded.exists())
        {
            uploaded.delete();
        }

        File downloaded = new File("target/downloaded.txt");
        if (downloaded.exists())
        {
            downloaded.delete();
        }
    }
}
