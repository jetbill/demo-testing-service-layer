package com.application.jet.testing_service_layer.service;

import com.application.jet.testing_service_layer.model.Employee;

import java.util.List;
import java.util.Optional;
public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(long id);
    Employee updateEmployee(Employee updatedEmployee, Long id);
    void deleteEmployee(long id);


}
