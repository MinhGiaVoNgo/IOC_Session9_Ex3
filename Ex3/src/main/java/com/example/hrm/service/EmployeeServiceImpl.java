package com.example.hrm.service;

import com.example.hrm.dto.EmployeeCreateDTO;
import com.example.hrm.entity.Department;
import com.example.hrm.entity.Employee;
import com.example.hrm.repository.DepartmentRepository;
import com.example.hrm.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public Employee createEmployee(EmployeeCreateDTO dto) {
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Phòng ban không tồn tại"));

        Employee employee = Employee.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .salary(dto.getSalary())
                .department(department)
                .build();

        return employeeRepository.save(employee);
    }
}
