package com.application.jet.testing_service_layer.service.impl;

import com.application.jet.testing_service_layer.exception.ResourceNotFoundException;
import com.application.jet.testing_service_layer.model.Employee;
import com.application.jet.testing_service_layer.repository.EmployeeRepository;
import com.application.jet.testing_service_layer.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> savedEmployee = employeeRepository
                .findByEmail(employee
                        .getEmail());
        if(savedEmployee.isPresent()){
            throw new ResourceNotFoundException("Employee already exists with given email: "
                    + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee, Long id) {

        return employeeRepository.save(updatedEmployee);
    }

    @Override
    public void deleteEmployee(long id) {
        employeeRepository.deleteById(id);
    }
}
