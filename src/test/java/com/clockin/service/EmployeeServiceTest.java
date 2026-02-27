package com.clockin.service;

import com.clockin.dto.request.EmployeeRequest;
import com.clockin.dto.request.EmployeeUpdate;
import com.clockin.dto.response.EmployeeResponse;
import com.clockin.exceptions.EmployeeNotFoundException;
import com.clockin.exceptions.InvalidDataException;
import com.clockin.model.Employee;
import com.clockin.model.enums.ContractType;
import com.clockin.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should Throws an Employee NotFoundException when no employees were found")
    public void ShouldTrowsAExceptionWhenEmployeeNotFound() {

        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, employeeService::getAllEmployees);

        assertEquals("No Employees were Found!", exception.getMessage());
        verify(employeeRepository, times(1)).findAll();

    }

    @Test
    @DisplayName("Returns a list of employees.")
    public void returnAListOfEmployee() {

        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("name", ContractType.CLT));
        employees.get(0).setId(1L);

        when(employeeRepository.findAll()).thenReturn(employees);

        List<EmployeeResponse> employeesDto = employeeService.getAllEmployees();

        assertNotNull(employeesDto);
        assertEquals(1, employeesDto.size());

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Return a Employee by Id")
    public void returnAEmployeeById() {

        Employee employee = new Employee("name", ContractType.CLT);
        employee.setId(1L);

        when(employeeRepository.findById(eq(1L))).thenReturn(Optional.of(employee));

        EmployeeResponse employee1 = employeeService.getEmployeeById(1L);

        assertNotNull(employee1);
        assertEquals("name", employee1.name());
        assertEquals(ContractType.CLT, employee1.contractType());
    }

    @Test
    @DisplayName("Should register Employee successfully")
    public void registerEmployeeSuccessfully() {

        EmployeeRequest employeeRequest = new EmployeeRequest("Alex", ContractType.CLT);
        Employee employee = new Employee(employeeRequest.name(), employeeRequest.contractType());

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeService.registerEmployee(employeeRequest);

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should throw EmployeeNotFoundException")
    public void updateEmployee() {

        EmployeeUpdate employeeUpdate = new EmployeeUpdate("name", "CLT");

        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(1L, employeeUpdate));

        assertEquals("Employee not found.", exception.getMessage());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw an exception for invalid names")
    public void throwExceptionForInvalidNames() {

        EmployeeUpdate employeeUpdate = new EmployeeUpdate("aa", "CLT");
        Employee employee = new Employee("Alex", ContractType.CLT);
        employee.setId(1L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> employeeService.updateEmployee(1L, employeeUpdate));

        assertEquals("Invalid Name!", exception.getMessage());
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should throw an exception for invalid contract types")
    public void throwAnExceptionForInvalidContractType() {

        EmployeeUpdate employeeUpdate = new EmployeeUpdate("Alex", "");
        Employee employee = new Employee("Alex", ContractType.CLT);
        employee.setId(1L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> employeeService.updateEmployee(1L, employeeUpdate));

        assertEquals("Select a valid contract type (CLT/ESTAGIO)", exception.getMessage());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should update Employee successfully")
    public void updateEmployeeSuccessfully() {

        EmployeeUpdate employeeUpdate = new EmployeeUpdate("João", "estagio");
        Employee employee = new Employee("Alex", ContractType.CLT);
        employee.setId(1L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.updateEmployee(1L, employeeUpdate);

        assertEquals("João", employee.getName());
        assertEquals(ContractType.ESTAGIO, employee.getContractType());
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(employee);
    }
}