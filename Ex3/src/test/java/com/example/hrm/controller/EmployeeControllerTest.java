package com.example.hrm.controller;

import com.example.hrm.dto.EmployeeCreateDTO;
import com.example.hrm.entity.Department;
import com.example.hrm.entity.Employee;
import com.example.hrm.exception.GlobalExceptionHandler;
import com.example.hrm.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @Test
    @DisplayName("POST /api/v1/employees - Invalid phone ('12345') returns 400 with 'Số điện thoại không hợp lệ'")
    void testCreateEmployee_InvalidPhone() throws Exception {
        EmployeeCreateDTO dto = EmployeeCreateDTO.builder()
                .fullName("Nguyễn Văn A")
                .email("vana@example.com")
                .phone("12345")
                .salary(10000000L)
                .departmentId(1L)
                .build();

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.phone").value("Số điện thoại không hợp lệ"));
    }

    @Test
    @DisplayName("POST /api/v1/employees - Valid employee data returns 201 Created")
    void testCreateEmployee_Success() throws Exception {
        EmployeeCreateDTO dto = EmployeeCreateDTO.builder()
                .fullName("Nguyễn Văn A")
                .email("vana@example.com")
                .phone("0987654321")
                .salary(10000000L)
                .departmentId(1L)
                .build();

        Department department = Department.builder()
                .id(1L)
                .name("Phòng Kỹ thuật")
                .build();

        Employee saved = Employee.builder()
                .id(1L)
                .fullName("Nguyễn Văn A")
                .email("vana@example.com")
                .phone("0987654321")
                .salary(10000000L)
                .department(department)
                .build();

        when(employeeService.createEmployee(any(EmployeeCreateDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Nguyễn Văn A"))
                .andExpect(jsonPath("$.email").value("vana@example.com"))
                .andExpect(jsonPath("$.phone").value("0987654321"))
                .andExpect(jsonPath("$.salary").value(10000000))
                .andExpect(jsonPath("$.department.id").value(1));
    }
}
