package com.elms.employee_leave_management.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "leave_requests")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;  // e.g., SICK, CASUAL, EARNED, UNPAID

    @Enumerated(EnumType.STRING)
    private LeaveStatus status = LeaveStatus.PENDING;   // PENDING, APPROVED, REJECTED

    private LocalDateTime appliedOn = LocalDateTime.now();

    private LocalDateTime decisionDate;

    // ✅ Employee who applied for leave
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties({"leaveRequests", "manager"})
    private Employee employee;

    // ✅ Manager who approves/rejects
    @ManyToOne
    @JoinColumn(name = "manager_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties({"leaveRequests", "manager"})
    private Employee manager;
}
