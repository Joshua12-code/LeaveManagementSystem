package com.elms.employee_leave_management.controller;

import com.elms.employee_leave_management.entity.Employee;
import com.elms.employee_leave_management.entity.LeaveRequest;
import com.elms.employee_leave_management.entity.LeaveStatus;
import com.elms.employee_leave_management.entity.LeaveType;
import com.elms.employee_leave_management.repository.EmployeeRepository;
import com.elms.employee_leave_management.service.EmailService;
import com.elms.employee_leave_management.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leaves")
@CrossOrigin(origins = "http://localhost:8080")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public List<LeaveRequest> getAllLeaves() {
        return leaveRequestService.getAllLeaveRequests();
    }

    // ✅ Fetch specific leave by ID (for manager view)
    @GetMapping("/{id}")
    public LeaveRequest getLeaveById(@PathVariable Long id) {
        return leaveRequestService.getLeaveById(id);
    }

    @PostMapping("/apply")
    public Map<String, Object> applyLeave(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long employeeId = Long.parseLong(payload.get("employeeId").toString());
            String leaveType = payload.get("leaveType").toString();
            String fromDate = payload.get("fromDate").toString();
            String toDate = payload.get("toDate").toString();
            String reason = payload.get("reason").toString();

            // ✅ Fetch employee
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

            // ✅ Create leave request
            LeaveRequest leave = new LeaveRequest();
            leave.setEmployee(employee);
            leave.setLeaveType(LeaveType.valueOf(leaveType.toUpperCase()));
            leave.setStartDate(LocalDate.parse(fromDate));
            leave.setEndDate(LocalDate.parse(toDate));
            leave.setReason(reason);
            leave.setStatus(LeaveStatus.PENDING);

            // ✅ Save leave
            LeaveRequest savedLeave = leaveRequestService.saveLeaveRequest(leave);

            // ✅ Send email to manager
            if (employee.getManager() != null && employee.getManager().getEmail() != null) {
                emailService.sendLeaveRequestEmail(
                        employee.getManager().getEmail(),
                        savedLeave.getId(),
                        employee.getName()
                );
            }

            response.put("status", "success");
            response.put("message", "Leave applied successfully and email sent to manager!");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to apply leave: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/employee/{employeeId}")
    public List<LeaveRequest> getLeavesByEmployee(@PathVariable Long employeeId) {
        return leaveRequestService.getLeavesByEmployeeId(employeeId);
    }

    @PutMapping("/{id}/status")
    public LeaveRequest updateStatus(@PathVariable Long id, @RequestParam String status) {
        return leaveRequestService.updateLeaveStatus(id, status);
    }
    @GetMapping("/test-email")
public String testEmail() {
    emailService.sendLeaveRequestEmail("manager@example.com", 0L, "Test Employee");
    return "Check logs for email status!";
}

    @DeleteMapping("/{id}")
    public void deleteLeave(@PathVariable Long id) {
        leaveRequestService.deleteLeaveRequest(id);
    }
}
