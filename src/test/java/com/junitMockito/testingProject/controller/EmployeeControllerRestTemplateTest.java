package com.junitMockito.testingProject.controller;

import com.junitMockito.testingProject.model.Employee;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static  org.junit.Assert.*;

//A diferencia de los anteriores debe estar levantada la aplicacion
@TestMethodOrder(MethodOrderer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerRestTemplateTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private Employee employee;
    private final String apiUrl = "http://localhost:8080/api/employees/";

    @BeforeEach
    void setup(){
        employee = Employee.builder().name("Juan").email("juanpipre@outlook.com").lastname("Pregliasco").build();
    }

    @Test
    @Order(1)
    void saveEmployeeTest(){                                              //3 parametros, la url, lo que devolvera y la clase.
        ResponseEntity<Employee> response = testRestTemplate.postForEntity(apiUrl+"save",employee,Employee.class);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON,response.getHeaders().getContentType());

        Employee createdEmployee = response.getBody();

        assertNotNull(createdEmployee);
        assertEquals("Juan",createdEmployee.getName());
    }

    @Test
    @Order(2)
    void getEmployees(){
        ResponseEntity<Employee[]> response = testRestTemplate.getForEntity(apiUrl+"get",Employee[].class);
        List<Employee> employees = Arrays.asList(response.getBody());

        assertEquals(HttpStatus.OK,response.getStatusCode());

        //assertEquals(1,employees.size());
        assertEquals("Juan",employees.get(0).getName());
    }

    @Test
    @Order(3)
    void getEmployee(){
        ResponseEntity<Employee> response = testRestTemplate.getForEntity(apiUrl+"get/1",Employee.class);
        Employee savedEmployee = response.getBody();

        assertEquals(MediaType.APPLICATION_JSON,response.getHeaders().getContentType());

        assertNotNull(savedEmployee);

        assertEquals("Juan",employee.getName());
    }

    @Test
    @Order(4)
    void deleteEmployeeTest(){
        ResponseEntity<Employee[]> response = testRestTemplate.getForEntity(apiUrl  + "get",Employee[].class);
        List<Employee> empleados = Arrays.asList(response.getBody());
        assertEquals(1,empleados.size());

        Map<String,Long> pathVariables = new HashMap<>();
        pathVariables.put("employeeId",1L);
        ResponseEntity<Void> exchange = testRestTemplate.exchange(apiUrl + "delete/{employeeId}", HttpMethod.DELETE,null,Void.class,pathVariables);

        assertEquals(HttpStatus.OK,exchange.getStatusCode());
        assertFalse(exchange.hasBody());

        response = testRestTemplate.getForEntity(apiUrl  + "get",Employee[].class);
        empleados = Arrays.asList(response.getBody());
        assertEquals(0,empleados.size());

    }
}
