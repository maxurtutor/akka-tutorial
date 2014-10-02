package org.maxur.spammer;

import org.slf4j.Logger;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Maxim Yunusov
 * @version 1.0 14.09.2014
 */
public class MailServiceJavaxImpl implements MailService {

    private static Logger LOGGER = getLogger(MailServiceJavaxImpl.class);

    public static final int DEFAULT_SMTP_PORT = 25;

    public static final String DEFAULT_SMTP_HOST = "127.0.0.1";

    private final String fromAddress;

    private final Properties props;


    public MailServiceJavaxImpl(final String fromAddress) {
        this(fromAddress, DEFAULT_SMTP_HOST, DEFAULT_SMTP_PORT);
    }

    public MailServiceJavaxImpl(final String fromAddress, final String host, final int port) {
        this.fromAddress = fromAddress;
        props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
    }


    @Override
    public void send(final Mail mail) throws MessagingException {
        try {
            Transport.send(makeMessageBy(mail));
        } catch (MessagingException e) {
            LOGGER.error("Unable to send email", e);
            throw e;
        }
    }

    private Message makeMessageBy(final Mail mail) throws MessagingException {
        final Message message = prepareMessage(mail, getSession());
        final Multipart multipart = new MimeMultipart();
        addTextPart(mail, multipart);
        message.setContent(multipart);
        return message;
    }

    private void addTextPart(final Mail mail, final Multipart multipart) throws MessagingException {
        final BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(mail.getBody());
        multipart.addBodyPart(messageBodyPart);
    }

    private Message prepareMessage(final Mail mail, final Session session) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromAddress));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getToAddress()));
        message.setSubject(mail.getSubject());
        return message;
    }

    private Session getSession() {
        return Session.getInstance(props);
    }

    @Override
    public void done() {
        // TODO
    }
}
