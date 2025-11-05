package com.elms.employee_leave_management.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EmailService {

    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";
    private static final String API_KEY = System.getenv("BREVO_API_KEY");

    // ✅ Replace with your Render-deployed frontend URL
    private static final String APP_BASE_URL = "https://leavemanagementsystem-kcww.onrender.com";

    public void sendLeaveRequestEmail(String managerEmail, Long leaveRequestId, String employeeName) {
        try {
            String subject = "New Leave Request from " + employeeName;
            String message = "<p>Hello Manager,</p>" +
                    "<p>You have received a new leave request from <b>" + employeeName + "</b>.</p>" +
                    "<p>👉 <a href='" + APP_BASE_URL + "/manager-login.html?redirect=manager-leave-view.html?leaveId=" + leaveRequestId + "'>View Request</a></p>" +
                    "<p>Best regards,<br/>Employee Leave Management System</p>";

            sendEmail(managerEmail, subject, message);
            System.out.println("✅ Email sent successfully to " + managerEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Generic method to send any email via Brevo API
    private void sendEmail(String to, String subject, String htmlContent) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = Map.of(
                "sender", Map.of("name", "ELMS System", "email", "noreply@elms.com"),
                "to", new Object[]{Map.of("email", to)},
                "subject", subject,
                "htmlContent", htmlContent
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        restTemplate.postForEntity(BREVO_URL, request, String.class);
    }

    // Optional: Test email
    public void sendTestEmail(String toEmail) {
        sendLeaveRequestEmail(toEmail, 0L, "Test Employee");
    }
}
