package com.application.jet.testing_service_layer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.application.jet.testing_service_layer.exception.ResourceNotFoundException;
import com.application.jet.testing_service_layer.model.Employee;
import com.application.jet.testing_service_layer.repository.EmployeeRepository;
import com.application.jet.testing_service_layer.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .id(1L)
                .firstName("Jetbill")
                .lastName("GoodName")
                .email("jetbill@gmail.com")
                .build();
    }

    @Test
    @DisplayName("JUnit test for saveEmployee method")
    void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
        //given
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        //when
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then
        assertThat(savedEmployee).isNotNull();

    }

    @Test
    @DisplayName("JUnit test for saveEmployee method which throws exception")
    void givenExistingEmail_whenSaveEmployee_thenThrowsException(){

        //given
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        //when
        assertThrows(ResourceNotFoundException.class, () -> {
                    employeeService.saveEmployee(employee);
                });
        //then
            verify(employeeRepository, never()).save(any(Employee.class));

    }
    @DisplayName("JUnit test for getAllEmployees method")
    @Test
    void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList(){
        // given
        Employee employeeTwo = Employee.builder()
                .id(2L)
                .firstName("Mary")
                .lastName("Smith")
                .email("mary@gmail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee, employeeTwo));
        // when
        List<Employee> employeeList = employeeService.getAllEmployees();
        // then
         assertThat(employeeList).hasSize(2);
    }

    @DisplayName("JUnit test for getAllEmployees method (negative scenario)")
    @Test
    void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList(){
        // given
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then
        assertThat(employeeList).isEmpty();

    }



}