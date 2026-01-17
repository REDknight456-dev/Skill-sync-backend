package com.skillsync.service.impl;

import com.skillsync.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromAddress;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendWelcomeEmail(String toEmail, String firstName) {
        String safeName = firstName == null || firstName.isBlank() ? "there" : firstName;
        String subject = "Welcome to Skill-Sync | Start Your Journey";
        String content = """
                <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; max-width: 640px; margin: auto; border: 1px solid #0b1f3a; border-radius: 12px; overflow: hidden; box-shadow: 0 10px 30px rgba(0,0,0,0.12);">
                    <div style="background: linear-gradient(135deg, #0b1f3a 0%%, #122b50 60%%, #1d4ed8 100%%); padding: 28px; text-align: center;">
                        <h1 style="color: #e2e8f0; margin: 0; font-size: 26px; letter-spacing: 0.5px;">Skill-Sync</h1>
                        <p style="color: #cbd5e1; margin: 8px 0 0; font-size: 14px;">Sync your skills with the future</p>
                    </div>
                    <div style="padding: 28px; background-color: #0f172a; color: #e2e8f0;">
                        <h2 style="margin: 0 0 12px; font-size: 22px; color: #e2e8f0;">Hello %s,</h2>
                        <p style="margin: 0 0 16px; color: #cbd5e1; line-height: 1.6;">
                            Welcome aboard! Your account is active and you can start exploring new courses curated by our architects.
                        </p>
                        <div style="text-align: center; margin: 28px 0;">
                            <a href="http://localhost:5173/login" style="background-color: #1d4ed8; color: #f8fafc; padding: 12px 24px; text-decoration: none; border-radius: 8px; font-weight: 700; display: inline-block; box-shadow: 0 8px 20px rgba(29,78,216,0.35);">
                                Access Your Dashboard
                            </a>
                        </div>
                        <p style="margin: 0; color: #94a3b8; font-size: 14px;">
                            <b>Tip:</b> Check the "New Skills" section to see the latest drops and stay ahead.
                        </p>
                    </div>
                    <div style="background-color: #0b1224; padding: 18px; text-align: center; color: #94a3b8; font-size: 12px;">
                        &copy; 2026 Skill-Sync Platform. All rights reserved.
                    </div>
                </div>
                """.formatted(safeName);

        sendHtml(toEmail, subject, content);
    }

    @Override
    public void sendCourseCreatedAdmin(String adminEmail, String courseTitle, String instructor) {
        String subject = "New course created: " + courseTitle;
        String content = """
                <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; max-width: 640px; margin: auto; border: 1px solid #0b1f3a; border-radius: 12px; overflow: hidden; box-shadow: 0 10px 30px rgba(0,0,0,0.12);">
                    <div style="background: linear-gradient(135deg, #0b1f3a 0%%, #122b50 60%%, #1d4ed8 100%%); padding: 24px; text-align: center;">
                        <h2 style="color: #e2e8f0; margin: 0; font-size: 22px;">New Course Published</h2>
                    </div>
                    <div style="padding: 24px; background-color: #0f172a; color: #e2e8f0;">
                        <p style="margin: 0 0 12px;">A new course has just been created:</p>
                        <p style="margin: 0 0 8px;"><b>Title:</b> %s</p>
                        <p style="margin: 0 0 8px;"><b>Instructor:</b> %s</p>
                        <p style="margin: 12px 0 0; color: #94a3b8; font-size: 13px;">You can review it in the admin dashboard.</p>
                    </div>
                    <div style="background-color: #0b1224; padding: 16px; text-align: center; color: #94a3b8; font-size: 12px;">
                        Skill-Sync Ops Notification
                    </div>
                </div>
                """.formatted(courseTitle, instructor);

        sendHtml(adminEmail, subject, content);
    }

    private void sendHtml(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException ex) {
            log.error("Failed to send email to {}", to, ex);
        }
    }
}
