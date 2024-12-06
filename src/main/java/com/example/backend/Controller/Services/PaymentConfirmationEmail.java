package com.example.backend.Controller.Services;

import com.example.backend.Model.Interface.Email;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public class PaymentConfirmationEmail implements Email {

    private JavaMailSender mailSender;
    private String clientName;
    private String saleDetails;

    public PaymentConfirmationEmail(JavaMailSender mailSender, String clientName, String saleDetails) {
        this.mailSender = mailSender;
        this.clientName = clientName;
        this.saleDetails = saleDetails;
    }

    @Override
    public void send(String toEmail) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom("no-reply@fakemail.com");
            helper.setTo(toEmail);
            helper.setSubject("Payment Confirmation");

            String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: auto; border: 1px solid #ddd; padding: 20px; border-radius: 8px; background-color: #f9f9f9;">
                    <div style="text-align: center; margin-bottom: 20px;">
                        <h1 style="color: #4CAF50; font-size: 24px;">Payment Confirmation</h1>
                        <p style="font-size: 16px; color: #666;">Your payment has been processed successfully!</p>
                    </div>
                    <div style="border-bottom: 1px solid #ddd; margin-bottom: 20px;"></div>
                    <p style="font-size: 16px;">Dear <strong>%s</strong>,</p>
                    <p style="font-size: 16px;">Thank you for your payment. Here are the details of your transaction:</p>
                    <div style="background-color: #f4f4f4; padding: 15px; border: 1px solid #ddd; border-radius: 4px;">
                        <pre style="font-size: 14px; margin: 0; white-space: pre-wrap; word-wrap: break-word;">%s</pre>
                    </div>
                    <p style="font-size: 16px; margin-top: 20px;">If you have any questions, please contact us at <a href="mailto:support@ejemplo.com" style="color: #4CAF50;">support@fakemail.com</a>.</p>
                    <div style="text-align: center; margin-top: 30px;">
                        <p style="font-size: 14px; color: #999;">Thank you for choosing our service!</p>
                    </div>
                </div>
            </body>
            </html>
        """.formatted(clientName, saleDetails);

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            System.out.println("Payment confirmation email sent successfully.");
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
