package com.junitMockito.testingProject.service.impl;

import com.junitMockito.testingProject.exception.ResourceNotFoundException;
import com.junitMockito.testingProject.model.Employee;
import com.junitMockito.testingProject.repository.IEmployeeRepository;
import com.junitMockito.testingProject.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements IEmployeeService {

    @Autowired
    private IEmployeeRepository employeeRepo;

    @Override
    public Employee saveEmployee(Employee employee) {
        if(employeeRepo.findByEmail(employee.getEmail()).isPresent()){
            throw new ResourceNotFoundException("El empleado con ese email ya existe "+employee.getEmail());
        }
      return  employeeRepo.save(employee);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepo.findAll();
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeRepo.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee) {
        return employeeRepo.save(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepo.deleteById(id);
    }
}
