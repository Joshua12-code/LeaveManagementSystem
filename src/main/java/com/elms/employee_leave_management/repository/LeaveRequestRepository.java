package com.elms.employee_leave_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elms.employee_leave_management.entity.Employee;
import com.elms.employee_leave_management.entity.LeaveRequest;
import com.elms.employee_leave_management.entity.LeaveStatus;
import com.elms.employee_leave_management.entity.LeaveType;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployee(Employee employee);

    List<LeaveRequest> findByManager(Employee manager);

    List<LeaveRequest> findByStatus(LeaveStatus status);

    List<LeaveRequest> findByLeaveType(LeaveType leaveType);
}
