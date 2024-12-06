package com.example.backend.Controller.Services;

import com.example.backend.Model.Interface.Email;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public class PasswordResetEmail implements Email {

    private JavaMailSender mailSender;
    private String resetLink;

    public PasswordResetEmail(JavaMailSender mailSender, String resetLink) {
        this.mailSender = mailSender;
        this.resetLink = resetLink;
    }

    @Override
    public void send(String toEmail) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom("no-reply@fakemail.com");
            helper.setTo(toEmail);
            helper.setSubject("Password Reset Request");

            String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: auto; border: 1px solid #ddd; padding: 20px; border-radius: 8px; background-color: #f9f9f9;">
                    <div style="text-align: center; margin-bottom: 20px;">
                        <h1 style="color: #FF5722; font-size: 24px;">Password Reset Request</h1>
                        <p style="font-size: 16px; color: #666;">We received a request to reset your password.</p>
                    </div>
                    <div style="border-bottom: 1px solid #ddd; margin-bottom: 20px;"></div>
                    <p style="font-size: 16px;">To reset your password, click the button below:</p>
                    <div style="text-align: center; margin: 20px 0;">
                        <a href="%s" style="display: inline-block; background-color: #FF5722; color: #fff; padding: 10px 20px; text-decoration: none; font-size: 16px; border-radius: 4px;">Reset Password</a>
                    </div>
                    <p style="font-size: 16px;">If you did not request this, please ignore this email. Your password will remain unchanged.</p>
                    <div style="text-align: center; margin-top: 30px;">
                        <p style="font-size: 14px; color: #999;">If you have any questions, contact us at <a href="mailto:support@ejemplo.com" style="color: #FF5722;">support@fakemail.com</a>.</p>
                    </div>
                </div>
            </body>
            </html>
        """.formatted(resetLink);

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            System.out.println("Password reset email sent successfully.");
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
