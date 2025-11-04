package com.elms.employee_leave_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elms.employee_leave_management.entity.Employee;
import com.elms.employee_leave_management.entity.Role;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    List<Employee> findByRole(Role role);

    List<Employee> findByManager(Employee manager);
}
