package com.elms.employee_leave_management.controller;

import com.elms.employee_leave_management.entity.Employee;
import com.elms.employee_leave_management.entity.LeaveRequest;
import com.elms.employee_leave_management.entity.LeaveStatus;
import com.elms.employee_leave_management.entity.LeaveType;
import com.elms.employee_leave_management.repository.EmployeeRepository;
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

    @GetMapping
    public List<LeaveRequest> getAllLeaves() {
        return leaveRequestService.getAllLeaveRequests();
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

            // ✅ Fetch employee entity
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

            // ✅ Create LeaveRequest
            LeaveRequest leave = new LeaveRequest();
            leave.setEmployee(employee);
            leave.setLeaveType(LeaveType.valueOf(leaveType.toUpperCase()));
            leave.setStartDate(LocalDate.parse(fromDate));
            leave.setEndDate(LocalDate.parse(toDate));
            leave.setReason(reason);
            leave.setStatus(LeaveStatus.PENDING);

            // ✅ Save
            leaveRequestService.saveLeaveRequest(leave);

            response.put("status", "success");
            response.put("message", "Leave applied successfully!");
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

    @DeleteMapping("/{id}")
    public void deleteLeave(@PathVariable Long id) {
        leaveRequestService.deleteLeaveRequest(id);
    }
}
