package com.junitMockito.testingProject.controller;

import com.junitMockito.testingProject.model.Employee;
import com.junitMockito.testingProject.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private IEmployeeService employeeService;

    @GetMapping("/get")
    public List<Employee> getAll(){
        return employeeService.findAll();
    }

    @GetMapping("/get/{employeeId}")
    public ResponseEntity<Employee> findById(@PathVariable Long employeeId){
        return employeeService.findById(employeeId).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee saveEmployee(@RequestBody Employee employee){
        employeeService.saveEmployee(employee);
        return employee;
    }

    @PutMapping("/update/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable long employeeId, @RequestBody Employee employee){
        return employeeService.findById(employeeId)
                .map(savedEmployee -> {
                    savedEmployee.setName(employee.getName());
                    savedEmployee.setLastname(employee.getLastname());
                    savedEmployee.setEmail(employee.getEmail());

                    Employee updatedEmployee = employeeService.updateEmployee(savedEmployee);
                    return new ResponseEntity<>(savedEmployee,HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long employeeId){
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok().build();
    }



}
