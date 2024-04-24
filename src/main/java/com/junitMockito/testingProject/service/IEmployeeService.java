package com.junitMockito.testingProject.service;

import com.junitMockito.testingProject.model.Employee;

import java.util.List;
import java.util.Optional;

public interface IEmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> findAll();
    Optional<Employee> findById(Long id);
    Employee updateEmployee(Employee updatedEmployee);
    void deleteEmployee(Long id);
}
