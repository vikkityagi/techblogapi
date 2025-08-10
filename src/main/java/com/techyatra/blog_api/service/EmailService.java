package com.techyatra.blog_api.service;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, Long> otpExpiry = new ConcurrentHashMap<>();

    private static final long EXPIRY_DURATION = 5 * 60 * 1000; // 5 minutes

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public void sendOtpEmail(String toEmail, String otp) throws Exception {
        String host = "smtp.gmail.com";
        final String username = "vikkityagi1998@gmail.com";
        final String password = "zjohdigegxoyjiyx";

        // ✅ Basic email format check
        if (!EMAIL_PATTERN.matcher(toEmail).matches()) {
            throw new Exception("Invalid email format: " + toEmail);
        }

        // ✅ Domain MX record check before sending
        String domain = toEmail.substring(toEmail.indexOf("@") + 1);
        if (!isDomainValid(domain)) {
            throw new Exception("Email domain is invalid: " + domain);
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // ✅ Set envelope sender for bounce handling
        props.put("mail.smtp.from", username);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        session.setDebug(true); // ✅ Enable SMTP debug logs

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("TechYatraWithVikki");
            message.setText("Your OTP Code is: " + otp + "\nThis OTP will expire in 5 minutes.");

            Transport transport = session.getTransport("smtp");
            try {
                transport.connect(host, username, password);
                transport.sendMessage(message, message.getAllRecipients());
                System.out.println("Email sent successfully ✅");

                // ✅ Store OTP with expiry
                otpStorage.put(toEmail, otp);
                otpExpiry.put(toEmail, System.currentTimeMillis() + EXPIRY_DURATION);

            } finally {
                transport.close();
            }

        } catch (SendFailedException sfe) {
            if (sfe.getInvalidAddresses() != null && sfe.getInvalidAddresses().length > 0) {
                throw new Exception("Invalid email address: " + sfe.getInvalidAddresses()[0]);
            }
            if (sfe.getValidUnsentAddresses() != null && sfe.getValidUnsentAddresses().length > 0) {
                throw new Exception("Could not send to: " + sfe.getValidUnsentAddresses()[0]);
            }
            throw new Exception("SendFailedException: " + sfe.getMessage());

        } catch (MessagingException e) {
            throw new Exception("SMTP Error: " + e.getMessage());
        }
    }

    public boolean validateOtp(String email, String otp) {
        if (!otpStorage.containsKey(email))
            return false;

        long expiryTime = otpExpiry.get(email);
        if (System.currentTimeMillis() > expiryTime) {
            otpStorage.remove(email);
            otpExpiry.remove(email);
            return false;
        }

        if (otpStorage.get(email).equals(otp)) {
            otpStorage.remove(email);
            otpExpiry.remove(email);
            return true;
        }
        return false;
    }

    public long getTime() {
        return this.EXPIRY_DURATION;
    }

    private boolean isDomainValid(String domain) {
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            DirContext ctx = new InitialDirContext(env);
            Attributes attrs = ctx.getAttributes(domain, new String[] { "MX" });
            return attrs != null && attrs.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
