package com.elms.employee_leave_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // ✅ Replace with your Render-deployed frontend URL
    private static final String APP_BASE_URL = "https://leavemanagementsystem-kcww.onrender.com";

    public void sendLeaveRequestEmail(String managerEmail, Long leaveRequestId, String employeeName) {
        try {
            String subject = "New Leave Request from " + employeeName;
            String body = "Hello Manager,\n\n" +
                    "You have received a new leave request from " + employeeName + ".\n\n" +
                    "👉 Click below to view the request:\n" +
                    APP_BASE_URL + "/manager-login.html?redirect=manager-leave-view.html?leaveId=" + leaveRequestId +
                    "\n\nBest regards,\nEmployee Leave Management System";

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("9ae57c001@smtp-brevo.com"); // your Brevo sender
            message.setTo(managerEmail);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            System.out.println("✅ Email sent successfully to " + managerEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Optional test endpoint call
    public void sendTestEmail(String toEmail) {
        sendLeaveRequestEmail(toEmail, 0L, "Test Employee");
    }
}
