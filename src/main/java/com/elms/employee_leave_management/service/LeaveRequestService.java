package com.elms.employee_leave_management.service;

import com.elms.employee_leave_management.entity.Employee;
import com.elms.employee_leave_management.entity.LeaveRequest;
import com.elms.employee_leave_management.entity.LeaveStatus;
import com.elms.employee_leave_management.repository.EmployeeRepository;
import com.elms.employee_leave_management.repository.LeaveRequestRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository,
                               EmployeeRepository employeeRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public LeaveRequest saveLeaveRequest(LeaveRequest leaveRequest) {
        return leaveRequestRepository.save(leaveRequest);
    }

    @Transactional
    public void deleteLeaveRequest(Long id) {
        leaveRequestRepository.deleteById(id);
    }

    public LeaveRequest getLeaveById(Long id) {
        Optional<LeaveRequest> lr = leaveRequestRepository.findById(id);
        return lr.orElse(null);
    }

    public List<LeaveRequest> getLeavesByEmployeeId(Long employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        return employee.map(leaveRequestRepository::findByEmployee).orElse(List.of());
    }

    public LeaveRequest updateLeaveStatus(Long id, String status) {
        LeaveRequest leaveRequest = getLeaveById(id);
        if (leaveRequest != null) {
            try {
                LeaveStatus leaveStatus = LeaveStatus.valueOf(status.toUpperCase());
                leaveRequest.setStatus(leaveStatus);
                leaveRequest.setDecisionDate(java.time.LocalDateTime.now());
                return leaveRequestRepository.save(leaveRequest);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid status value: " + status);
            }
        }
        return null;
    }
}
