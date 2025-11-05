package com.elms.employee_leave_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base.url}")
    private String appBaseUrl;

    public void sendLeaveRequestEmail(String managerEmail, Long leaveRequestId, String employeeName) {
        String subject = "New Leave Request from " + employeeName;

        String message = "Hello Manager,\n\n"
                + "You have received a new leave request from " + employeeName + ".\n\n"
                + "👉 Click below to view the request:\n"
                + appBaseUrl + "/manager-leave-view.html?leaveId=" + leaveRequestId + "\n\n"
                + "Best regards,\nEmployee Leave Management System";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromEmail);
        mailMessage.setTo(managerEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
