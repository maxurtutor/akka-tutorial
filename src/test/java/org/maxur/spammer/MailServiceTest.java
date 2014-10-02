package org.maxur.spammer;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>10/1/2014</pre>
 */
public class MailServiceTest {

    private MailService service;

    @Before
    public void setUp() throws Exception {
        service = new MailServiceJavaxImpl("sender@here.com");
    }

    @Test
    public void testSend() throws Exception {
        final SimpleSmtpServer server = SimpleSmtpServer.start();
        service.send(
                Mail.builder()
                        .subject("Test")
                        .body("Test Body")
                        .toAddress("receiver@there.com")
                        .build()
        );
        server.stop();

        assertEquals(1, server.getReceivedEmailSize());
        final Iterator<SmtpMessage> iterator = server.getReceivedEmail();
        SmtpMessage email = iterator.next();
        assertEquals("Test", email.getHeaderValue("Subject"));

        assertTrue(email.getBody().contains("Test Body"));
    }


}
