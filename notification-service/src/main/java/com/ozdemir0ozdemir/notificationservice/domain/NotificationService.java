package com.ozdemir0ozdemir.notificationservice.domain;

import com.ozdemir0ozdemir.notificationservice.NotificationServiceProperties;
import com.ozdemir0ozdemir.notificationservice.domain.models.OrderCancelledEvent;
import com.ozdemir0ozdemir.notificationservice.domain.models.OrderCreatedEvent;
import com.ozdemir0ozdemir.notificationservice.domain.models.OrderDeliveredEvent;
import com.ozdemir0ozdemir.notificationservice.domain.models.OrderErrorEvent;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender mailSender;
    private final NotificationServiceProperties properties;

    NotificationService(JavaMailSender mailSender, NotificationServiceProperties properties) {
        this.mailSender = mailSender;
        this.properties = properties;
    }

    public void sendOrderNotification(OrderCreatedEvent event) {
        String message =
                """
                ====================================================
                             Order Created Notification
                ----------------------------------------------------
                Dear %s,
                Your order with number: %s has been created
                successfully.

                Thanks,
                BookStore Team
                ====================================================
                """
                        .formatted(event.customer().name(), event.orderNumber());

        log.info("\n{}", message);
        sendEmail(event.customer().email(), "Object Created Notification", message);
    }

    public void sendOrderNotification(OrderDeliveredEvent event) {
        String message =
                """
                ====================================================
                             Order Delivered Notification
                ----------------------------------------------------
                Dear %s,
                Your order with number: %s has been delivered
                successfully.

                Thanks,
                BookStore Team
                ====================================================
                """
                        .formatted(event.customer().name(), event.orderNumber());

        log.info("\n{}", message);
        sendEmail(event.customer().email(), "Object Delivered Notification", message);
    }

    public void sendOrderNotification(OrderCancelledEvent event) {
        String message =
                """
                ====================================================
                             Order Cancelled Notification
                ----------------------------------------------------
                Dear %s,
                Your order with number: %s has been cancelled.


                Thanks,
                BookStore Team
                ====================================================
                """
                        .formatted(event.customer().name(), event.orderNumber());

        log.info("\n{}", message);
        sendEmail(event.customer().email(), "Object Cancelled Notification", message);
    }

    public void sendOrderNotification(OrderErrorEvent event) {
        String message =
                """
                ====================================================
                             Order Error Notification
                ----------------------------------------------------
                Dear Support Team,
                The order with number: %s has been failed.

                Thanks,
                BookStore Team
                ====================================================
                """
                        .formatted(event.orderNumber());

        log.info("\n{}", message);
        sendEmail(properties.supportEmail(), "Object Error Notification", message);
    }

    private void sendEmail(String email, String subject, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(properties.supportEmail());
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(message);

            mailSender.send(mimeMessage);
            log.info("Email send to: {}", email);
        } catch (Exception e) {
            throw new RuntimeException("Error while sending email!", e);
        }
    }
}
