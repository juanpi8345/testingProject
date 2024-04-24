package com.junitMockito.testingProject.repository;

import com.junitMockito.testingProject.model.Employee;
import static  org.junit.Assert.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

//Vamos a poder realizar pruebas a la entidad y repositorios, o sea solo a la capa de jpa
@DataJpaTest
public class EmployeeRepositoryTests  {
    @Autowired
    private IEmployeeRepository employeeRepository;

    private Employee employee;

    //Antes de que se ejecute cada test
    @BeforeEach
    void setup(){
         employee = Employee.builder().name("Juan").email("juanpipre@outlook.com").lastname("Pregliasco").build();
    }

    @DisplayName("Test para guardar empleado")
    @Test
    public void saveEmployee(){
        //BDD se centra en el comportamiento mientras TDD en la prueba unitaria
        //Given condicion dada (Dado el empleado del setup)

        //When cuando ocurre (Cuando guardo el empleado), aca va lo que se va a probar
        Employee savedEmployee = employeeRepository.save(employee);
        //Then verificar salida (Verificar que no sea null y que su id sea mayor que 0)
        assertNotNull(savedEmployee);
        assertFalse(savedEmployee.getEmployeeId() <= 0);
    }

    @DisplayName("Test para obtener empleados")
    @Test
    public void getEmployees(){
        //Given
        Employee employee2 = Employee.builder().name("Pedro").lastname("Sanchez").email("pedro@hotmail.com").build();
        employeeRepository.save(employee);
        employeeRepository.save(employee2);
        //When
        List<Employee> employees = employeeRepository.findAll();
        //Then
        assertNotNull(employees);
        assertTrue(employees.size() == 2);
    }

    @DisplayName("Test para obtener un empleado")
    @Test
    public void getEmployee(){
        //Given
        employeeRepository.save(employee);
        //When
        Employee employeeGotten = employeeRepository.findById(employee.getEmployeeId()).get();
        //Then
        assertNotNull(employeeGotten);
    }

    @DisplayName("Test para actualizar empleado")
    @Test
    public void updateEmployee(){
        //Given
        employeeRepository.save(employee);
        //When
        Employee employeeSaved = employeeRepository.findById(employee.getEmployeeId()).get();
        employeeSaved.setName("Cristian");
        employeeSaved.setEmail("cristian@hotmail.com");
        employeeSaved.setLastname("Roberto");
        Employee employeeUpdated = employeeRepository.save(employeeSaved);
        //Then
        assertEquals(employeeUpdated.getEmail(),"cristian@hotmail.com");
        assertEquals(employeeUpdated.getName(),"Cristian");
        assertEquals(employeeUpdated.getLastname(),"Roberto");
    }

    @DisplayName("Test para eliminar empleado")
    @Test
    public void deleteEmployee(){
        //Given
        Employee employeeSaved  = employeeRepository.save(employee);
        //When
        employeeRepository.deleteById(employeeSaved.getEmployeeId());
        //Then
        Employee employeeDeleted = employeeRepository.findById(employeeSaved.getEmployeeId()).orElse(null);
        assertNull(employeeDeleted);
    }

}
