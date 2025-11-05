package com.elms.employee_leave_management.service;

import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailService {

    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";
    private static final String API_KEY = System.getenv("BREVO_API_KEY");

    // Frontend base URL
    private static final String APP_BASE_URL = "https://leavemanagementsystem-kcww.onrender.com";

    /**
     * Send a leave request email to manager with a link to login and redirect to leave view
     */
    @Async
    public void sendLeaveRequestEmail(String managerEmail, Long leaveRequestId, String employeeName) {
        if (managerEmail == null || managerEmail.isEmpty()) {
            System.err.println("❌ Manager email is null or empty, skipping email.");
            return;
        }

        try {
            // Build login URL with redirect to leave view and URL-encode redirect
            String redirectPath = "manager-leave-view.html?leaveId=" + leaveRequestId;
            String encodedRedirect = URLEncoder.encode(redirectPath, StandardCharsets.UTF_8);
            String redirectUrl = APP_BASE_URL + "/manager-login.html?redirect=" + encodedRedirect;

            String subject = "New Leave Request from " + employeeName;
            String htmlMessage = "<p>Hello Manager,</p>" +
                    "<p>You have received a new leave request from <b>" + employeeName + "</b>.</p>" +
                    "<p>👉 <a href='" + redirectUrl + "'>Click here to view the leave request</a></p>" +
                    "<p>Best regards,<br/>Employee Leave Management System</p>";

            sendEmail(managerEmail, subject, htmlMessage);

        } catch (Exception e) {
            System.err.println("❌ Failed to send leave request email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Internal method to send email using Brevo API
     */
    private void sendEmail(String to, String subject, String htmlContent) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Payload for Brevo API
        Map<String, Object> payload = Map.of(
                "sender", Map.of("name", "ELMS System", "email", "joshua21h332@gmail.com"), // Must be verified in Brevo
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

    /**
     * Optional method to test email delivery
     */
    public void sendTestEmail(String toEmail) {
        sendLeaveRequestEmail(toEmail, 0L, "Test Employee");
    }
}
