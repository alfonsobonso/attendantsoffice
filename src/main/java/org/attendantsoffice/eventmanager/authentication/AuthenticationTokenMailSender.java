package org.attendantsoffice.eventmanager.authentication;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

/**
 * Send the authentication ticket notification to a given user.
 */
@Component
public class AuthenticationTokenMailSender {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationTokenMailSender.class);
    private final boolean whitelistEmails;
    private final List<String> whitelistedEmails;
    private final MailSender mailSender;
    private final String emailFrom;


    public AuthenticationTokenMailSender(@Value("${app.email.whitelist:true}") boolean whitelistEmails,
            @Value("${app.email.whitelisted-emails:\"\"}") List<String> whitelistedEmails,
            MailSender mailSender,
            @Value("${app.email.from:test@example.com}") String emailFrom) {
        this.whitelistEmails = whitelistEmails;
        this.whitelistedEmails = whitelistedEmails;
        this.mailSender = mailSender;
        this.emailFrom = emailFrom;
    }

    public void mailAuthenticationTokenMail(Integer userId, String email, String token) {
        if (!isValidEmail(email)) {
            LOG.info("Token [{}] not sent to [{}] (User#{}) - email address is not whitelisted", token, email, userId);
            LOG.info("Access url: /token-access/{}", token);
            return;
        }

        SimpleMailMessage templateMessage = new SimpleMailMessage();
        templateMessage.setFrom(emailFrom);
        templateMessage.setSubject("TBD");
        templateMessage.setTo(email);
        templateMessage.setText("Your token: " + token);

        sendEmail(templateMessage);

    }

    private void sendEmail(SimpleMailMessage templateMessage) {
        try {
            this.mailSender.send(templateMessage);
        } catch (MailException e) {
            LOG.error("Failed to send authentication token email. Message: " + templateMessage, e);
        }
    }

    private boolean isValidEmail(String email) {
        return !whitelistEmails || whitelistedEmails.contains(email);
    }

}
