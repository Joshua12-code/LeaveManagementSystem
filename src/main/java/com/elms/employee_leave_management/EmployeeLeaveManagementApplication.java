package com.elms.employee_leave_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmployeeLeaveManagementApplication {

	public static void main(String[] args) {

		// 🔥 ADD THIS LINE
		System.out.println("RUNNING FROM: " + System.getProperty("user.dir"));

		SpringApplication.run(EmployeeLeaveManagementApplication.class, args);
	}
}