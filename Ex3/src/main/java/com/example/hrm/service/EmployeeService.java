package com.example.hrm.service;

import com.example.hrm.dto.EmployeeCreateDTO;
import com.example.hrm.entity.Employee;

public interface EmployeeService {
    Employee createEmployee(EmployeeCreateDTO dto);
}
