package com.clockin.service;

import com.clockin.dto.request.EmployeeRequest;
import com.clockin.dto.request.EmployeeUpdate;
import com.clockin.dto.response.EmployeeResponse;
import com.clockin.exceptions.EmployeeNotFoundException;
import com.clockin.exceptions.InvalidDataException;
import com.clockin.model.Employee;
import com.clockin.model.enums.ContractType;
import com.clockin.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<EmployeeResponse> getAllEmployees() {

        List<Employee> employees = repository.findAll();
        List<EmployeeResponse> employeeResponse = new ArrayList<>();

        if (employees.isEmpty()) {
            throw new EmployeeNotFoundException("No Employees were Found!");
        } else {
            for (Employee employee : employees) {
                employeeResponse.add(new EmployeeResponse(employee.getId(), employee.getName(), employee.getContractType()));
            }
            return employeeResponse;
        }
    }

    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = repository.findById(id).orElseThrow(EmployeeNotFoundException::new);
        return new EmployeeResponse(employee.getId(), employee.getName(), employee.getContractType());
    }

    public void registerEmployee(EmployeeRequest employeeRequest) {
        Employee employee = new Employee(employeeRequest.name(), employeeRequest.contractType());
        repository.save(employee);
    }

    public void updateEmployee(Long id, EmployeeUpdate employeeUpdate) {

        Employee employee = repository.findById(id).orElseThrow(EmployeeNotFoundException::new);

        if (employeeUpdate.name() != null) {
            if (employeeUpdate.name().length() < 3 || employeeUpdate.name().length() > 60) {
                throw new InvalidDataException("Invalid Name!");
            } else employee.setName(employeeUpdate.name());
        }

        if (employeeUpdate.contractType() != null) {
            if (employeeUpdate.contractType().equalsIgnoreCase("CLT") || employeeUpdate.contractType().equalsIgnoreCase("ESTAGIO")) {
                employee.setContractType(ContractType.valueOf(employeeUpdate.contractType().toUpperCase()));
            } else {
                throw new InvalidDataException("Select a valid contract type (CLT/ESTAGIO)");
            }
        }
        repository.save(employee);
    }
}
