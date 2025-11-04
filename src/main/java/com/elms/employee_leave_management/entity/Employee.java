package com.elms.employee_leave_management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String department;

    @Enumerated(EnumType.STRING)
    private Role role; // EMPLOYEE / MANAGER / ADMIN

    // self-join for manager relationship
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    // one employee can have many leave requests
@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LeaveRequest> leaveRequests;
}
