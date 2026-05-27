package com.elms.employee_leave_management.service;

import org.springframework.beans.factory.annotation.Value;
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

    // API key (with fallback to avoid crash)
    @Value("${brevo.api.key:dummy}")
    private String apiKey;

    // Base URL (local or prod)
    @Value("${app.base.url:http://localhost:8080}")
    private String appBaseUrl;

    /**
     * Send leave request email to manager
     */
    @Async
    public void sendLeaveRequestEmail(String managerEmail, Long leaveRequestId, String employeeName) {

        if (managerEmail == null || managerEmail.isEmpty()) {
            System.err.println("Manager email is null or empty. Skipping email.");
            return;
        }

        try {
            // Build redirect URL
            String redirectPath = "manager-leave-view.html?leaveId=" + leaveRequestId;
            String encodedRedirect = URLEncoder.encode(redirectPath, StandardCharsets.UTF_8);

            String redirectUrl = appBaseUrl + "/manager-login.html?redirect=" + encodedRedirect;

            String subject = "New Leave Request from " + employeeName;

            String htmlMessage =
                    "<p>Hello Manager,</p>" +
                    "<p>You have a new leave request from <b>" + employeeName + "</b>.</p>" +
                    "<p>👉 <a href='" + redirectUrl + "'>View Leave Request</a></p>" +
                    "<br/>" +
                    "<p>Regards,<br/>ELMS System</p>";

            sendEmail(managerEmail, subject, htmlMessage);

        } catch (Exception e) {
            System.err.println("Error sending leave request email:");
            e.printStackTrace();
        }
    }

    /**
     * Internal method to call Brevo API
     */
    private void sendEmail(String to, String subject, String htmlContent) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = Map.of(
                "sender", Map.of("name", "ELMS System", "email", "joshua21h332@gmail.com"),
                "to", new Object[]{Map.of("email", to)},
                "subject", subject,
                "htmlContent", htmlContent
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(BREVO_URL, request, String.class);

            System.out.println("Email sent to " + to + " | Response: " + response.getStatusCode());

        } catch (HttpClientErrorException e) {
            System.err.println("Brevo API error: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Unexpected error while sending email:");
            e.printStackTrace();
        }
    }

    /**
     * Test email method
     */
    public void sendTestEmail(String toEmail) {
        sendLeaveRequestEmail(toEmail, 0L, "Test Employee");
    }
}