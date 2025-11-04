package com.elms.employee_leave_management.controller;

import com.elms.employee_leave_management.dto.LoginRequest;
import com.elms.employee_leave_management.entity.Employee;
import com.elms.employee_leave_management.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:8080")
public class AuthController {

    private final EmployeeRepository employeeRepository;

    public AuthController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();

        return employeeRepository.findByEmail(loginRequest.getEmail())
                .filter(emp -> emp.getPassword().equals(loginRequest.getPassword()))
                .map(emp -> {
                    response.put("status", "success");
                    response.put("role", emp.getRole().toString());
                    response.put("employeeId", emp.getId());
                    response.put("dashboard", emp.getRole().toString().equals("MANAGER")
                            ? "manager-dashboard"
                            : "employee-dashboard");
                    return response;
                })
                .orElseGet(() -> {
                    response.put("status", "error");
                    response.put("message", "Invalid credentials");
                    return response;
                });
    }
}
