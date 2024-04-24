package com.junitMockito.testingProject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junitMockito.testingProject.model.Employee;
import com.junitMockito.testingProject.service.impl.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static  org.junit.Assert.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Nos sirve para poder probar los controladores
@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    //Nos sirve para agregar objetos simulados al contexto de la aplicacion.
    //Para probar peticiones HTTP
    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    void setup(){
       employee = Employee.builder().name("Juan").email("juanpipre@outlook.com").lastname("Pregliasco").build();
    }


    @Test
    void saveEmployee() throws Exception {
        //Given
        //El empleado del setup
        employee = Employee.builder().name("Juan").email("juanpipre@outlook.com").lastname("Pregliasco").build();
        given(employeeService.saveEmployee(any(Employee.class))).willAnswer((invocation)->invocation.getArgument(0));
        //When
        ResultActions response = mockMvc.perform(post("/api/employees/save").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //Response
        //Esperamos que el estado sea creado, el print es para que imprima
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(employee.getName()))
                .andExpect(jsonPath("$.email").value(employee.getEmail()))
                .andExpect(jsonPath("$.lastname").value(employee.getLastname()));
    }

    @Test
    void getEmployees() throws Exception{
        //Given
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder().name("Christian").lastname("Ramirez").email("c1@gmail.com").build());
        employeeList.add(Employee.builder().name("Gabriel").lastname("Ramirez").email("g1@gmail.com").build());
        employeeList.add(Employee.builder().name("Julen").lastname("Ramirez").email("cj@gmail.com").build());
        employeeList.add(Employee.builder().name("Biaggio").lastname("Ramirez").email("b1@gmail.com").build());
        employeeList.add(Employee.builder().name("Adrian").lastname("Ramirez").email("a@gmail.com").build());
        given(employeeService.findAll()).willReturn(employeeList);
        //When
        ResultActions response = mockMvc.perform(get("/api/employees/get"));
        //Then
        response.andExpect(status().isOk())
                .andDo(print())               //Funcion tamaño lista por eso los ()
                .andExpect(jsonPath("$.size()").value(employeeList.size()));
    }

    @Test
    void getEmployeeById() throws Exception{
        //Given
        long employeeId = 1L;
        given(employeeService.findById(employeeId)).willReturn(Optional.of(employee));
        //When
        ResultActions response = mockMvc.perform(get("/api/employees/get/{employeeId}",employeeId));
        //Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(employee.getName()));

    }

    @Test
    void getEmployeeByIdNotFound() throws Exception{
        //Given
        long employeeId = 1L;
        given(employeeService.findById(employeeId)).willReturn(Optional.empty());
        //When
        ResultActions response = mockMvc.perform(get("/api/employees/get/{employeeId}",employeeId));
        //Then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void updateEmployee() throws  Exception{
        //Given
        long employeeId = 1L;
        Employee updatedEmployee = Employee.builder().name("Pepe").email("p@outlook.com").lastname("Perez").build();
        given(employeeService.findById(employeeId)).willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation)-> invocation.getArgument(0));
        //When
        ResultActions response = mockMvc.perform(put("/api/employees/update/{employeeId}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                //Convierte la cadena a json para pasarselo en el metodo perform put
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(updatedEmployee.getName()));

    }

    @Test
    void updateEmployeeNotFound() throws  Exception{
        //Given
        long employeeId = 1L;
        Employee updatedEmployee = Employee.builder().name("Pepe").email("p@outlook.com").lastname("Perez").build();
        given(employeeService.findById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation)-> invocation.getArgument(0));
        //When
        ResultActions response = mockMvc.perform(put("/api/employees/update/{employeeId}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                //Convierte la cadena a json para pasarselo en el metodo perform put
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //Then
        response.andExpect(status().isNotFound());

    }

    @Test
    void deleteEmployee() throws Exception{
        //Given
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);
        //When
        ResultActions response = mockMvc.perform(delete("/api/employees/delete/{employeeId}",employeeId));
        //Then
        response.andExpect(status().isOk()).andDo(print());
    }
    


}
