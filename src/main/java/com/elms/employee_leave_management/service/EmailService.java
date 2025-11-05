package com.elms.employee_leave_management.service;

import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EmailService {

    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";
    private static final String API_KEY = System.getenv("BREVO_API_KEY");

    // ✅ Replace with your frontend URL
    private static final String APP_BASE_URL = "https://leavemanagementsystem-kcww.onrender.com";

    @Async
    public void sendLeaveRequestEmail(String managerEmail, Long leaveRequestId, String employeeName) {
        try {
            if (managerEmail == null || managerEmail.isEmpty()) {
                System.err.println("❌ Manager email is null or empty, skipping email.");
                return;
            }

            String subject = "New Leave Request from " + employeeName;
            String htmlMessage = "<p>Hello Manager,</p>" +
                    "<p>You have received a new leave request from <b>" + employeeName + "</b>.</p>" +
                    "<p>👉 <a href='" + APP_BASE_URL + "/manager-login.html?redirect=manager-leave-view.html?leaveId=" + leaveRequestId + "'>View Request</a></p>" +
                    "<p>Best regards,<br/>Employee Leave Management System</p>";

            sendEmail(managerEmail, subject, htmlMessage);

        } catch (Exception e) {
            System.err.println("❌ Failed to send leave request email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = Map.of(
                "sender", Map.of("name", "ELMS System", "email", "noreply@elms.com"), // Must be verified in Brevo
                "to", new Object[]{Map.of("email", to)},
                "subject", subject,
                "htmlContent", htmlContent
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(BREVO_URL, request, String.class);
            System.out.println("✅ Email sent to " + to + ". Brevo Response: " + response.getBody());
        } catch (HttpClientErrorException e) {
            System.err.println("❌ Brevo API Error: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("❌ Unexpected error while sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Optional test email
    public void sendTestEmail(String toEmail) {
        sendLeaveRequestEmail(toEmail, 0L, "Test Employee");
    }
}
