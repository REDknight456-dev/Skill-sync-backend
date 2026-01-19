package com.skillsync.service;

public interface EmailService {
    void sendWelcomeEmail(String toEmail, String name);
    void sendCourseCreatedAdmin(String adminEmail, String courseTitle, String instructor);
    void sendTwoFactorCode(String toEmail, String code);
}
