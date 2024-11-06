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

    @DisplayName("JUnit test for getEmployeeById method")
    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){
        // given
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        // when
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();
        // then
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for updateEmployee method")
    @Test
    void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        long employeeId = 1L;
        Employee forUpdate = Employee.builder()
                .id(employeeId)
                .firstName("Marcos")
                .lastName("Figueroa")
                .build();
        // given
        // Mocking findById to return the existing employee
        given(employeeRepository.findById(employeeId)).willReturn(Optional.of(employee));
        // Mocking save to return the updated employee
        Employee updatedEmployeeMock = new Employee(employeeId,
                "Marcos", "Figueroa", "jetbill@gmail.com");
        given(employeeRepository.save(employee)).willReturn(updatedEmployeeMock);


        // when
        Employee updatedEmployee = employeeService.updateEmployee(forUpdate, employeeId);

        // Then
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getId()).isEqualTo(employeeId);
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Marcos");
        assertThat(updatedEmployee.getLastName()).isEqualTo("Figueroa");
        assertThat(updatedEmployee.getEmail()).isEqualTo("jetbill@gmail.com");

        // Verify that findById was called once with the correct ID
        verify(employeeRepository).findById(employeeId);

        // Verify that save was called once with the updated employee
        verify(employeeRepository).save(employee);
    }

    @Test
    @DisplayName("JUnit test for updateEmployee method - Employee Not Found")
    void givenNonExistingEmployeeId_whenUpdateEmployee_thenThrowException(){
        // given
        Long employeeId = 2L;
        Employee forUpdate = Employee.builder()
                .id(employeeId)
                .firstName("María")
                .lastName("García")
                .build();

        //when
        // Mocking findById to return empty, simulating employee not found
        given(employeeRepository.findById(employeeId)).willReturn(Optional.empty());

        // Act & Assert (When & Then)
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.updateEmployee(forUpdate, employeeId);
        });

        // Verify that findById was called once with the correct ID
        verify(employeeRepository).findById(employeeId);

        // Verify that save was never called since employee was not found
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @DisplayName("JUnit test for deleteEmployee method")
    @Test
     void givenEmployeeId_whenDeleteEmployee_thenNothing(){
        // given
        long employeeId = 1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        // when
        employeeService.deleteEmployee(employeeId);

        // then
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}



