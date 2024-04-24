package com.junitMockito.testingProject.service;

import com.junitMockito.testingProject.exception.ResourceNotFoundException;
import com.junitMockito.testingProject.model.Employee;
import com.junitMockito.testingProject.repository.IEmployeeRepository;
import com.junitMockito.testingProject.service.impl.EmployeeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static  org.junit.Assert.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

//Sirve para cargar extensiones de Mockito
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    //Objeto simulado
    @Mock
    private IEmployeeRepository employeeRepository;

    //Para inyectar el objeto simulado employeeRepository dentro del servicio
    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    //Antes de que se ejecute cada test
    @BeforeEach
    void setup(){
        employee = Employee.builder().name("Juan").email("juanpipre@outlook.com").lastname("Pregliasco").build();
    }

    @DisplayName("Test para guardar un empleado")
    @Test
    public void saveEmployee(){
        //Given - condiciones dadas
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);
        //When - a probar
        Employee employeeSaved = employeeService.saveEmployee(employee);
        //Then - verificar salida
        assertNotNull(employeeSaved);
    }

    @DisplayName("Test para guardar un empleado con excepcion")
    @Test
    public void saveEmployeeWithThrow(){
        //Given - condiciones dadas
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
        //When - a probar
        assertThrows(ResourceNotFoundException.class,()->{
           employeeService.saveEmployee(employee);
        });
        //Then - verificar salida
        //Se verifica que el employeeRepostory nunca haya llamado al metodo guardar, ni haya guardado a un objeto de clase empleado
        verify(employeeRepository,never()).save(any(Employee.class));
    }

    @DisplayName("Test para obtener empleados")
    @Test
    public void getEmployees(){
        //Given
        Employee employee2 = Employee.builder().name("Jorge").lastname("Paco").email("pacojorge@hotmail.com").build();
        given(employeeRepository.findAll()).willReturn(List.of(employee,employee2));
        //When
        List<Employee> employees = employeeService.findAll();
        //Then
        assertEquals(employees.size(),2);

    }

    @DisplayName("Test para obtener lista vacia de empleados")
    @Test
    public void getEmptyList(){
        //Given
        given(employeeRepository.findAll()).willReturn(Collections.EMPTY_LIST);
        //When
        List<Employee> employees = employeeService.findAll();
        //Then
        assertTrue(employees.isEmpty());
    }

    @DisplayName("Test para obtener empleado por ID")
    @Test
    public void getEmployee(){
        //Given
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        //When
        Employee employee = employeeService.findById(1L).get();
        //Then
        assertNotNull(employee);
    }

    @DisplayName("Test para actualizar empleado")
    @Test
    public void updateEmployee(){
        //Given
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setLastname("Argenti");
        employee.setName("Pipo");
        employee.setEmail("pipoargenti@gmail.com");
        //When
        Employee employeeSaved = employeeService.updateEmployee(employee);
        //Ya con comparar un campo me doy cuenta si actualizo bien
        assertEquals(employeeSaved.getEmail(),"pipoargenti@gmail.com");
    }

    @DisplayName("Test para eliminar empleado")
    @Test
    public void deleteEmployee(){
        //Given
        Long employeeId = 1L;
        //Como el deleteById no retorna nada, le decimos que no hara nada
        willDoNothing().given(employeeRepository).deleteById(employeeId);
        //When
        employeeService.deleteEmployee(employeeId);
        //Then
        //Se verifica que se haya llamado una vez al metodo employeeRepository.deleteById(employeeId)
        verify(employeeRepository,times(1)).deleteById(employeeId);
    }
}
