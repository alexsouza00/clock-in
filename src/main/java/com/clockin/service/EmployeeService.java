package com.clockin.service;

import com.clockin.dto.request.EmployeeDto;
import com.clockin.dto.request.EmployeeUpdateDto;
import com.clockin.exceptions.DataBaseException;
import com.clockin.exceptions.EmployeeNotFoundException;
import com.clockin.exceptions.InvalidDataException;
import com.clockin.model.Employee;
import com.clockin.model.enums.ContractType;
import com.clockin.repository.EmployeeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getEmployees() {
        return repository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        Optional<Employee> employee = repository.findById(id);
        if (employee.isPresent()) {
            return employee.get();
        } else throw new EmployeeNotFoundException();
    }

    public void registerEmployee(EmployeeDto employeeDto) {

        if (employeeDto.contractType() != null) {
            if (employeeDto.contractType().equalsIgnoreCase("CLT") || employeeDto.contractType().equalsIgnoreCase("ESTAGIO")) {
                Employee employee = new Employee(employeeDto.name(), ContractType.valueOf(employeeDto.contractType().toUpperCase()));
                repository.save(employee);
            } else {
                throw new InvalidDataException("Select a valid contract type (CLT, ESTAGIO)");
            }
        }
    }

    public void updateEmployee(Long id, EmployeeUpdateDto employeeUpdateDto) {

        Employee employee = repository.findById(id).orElseThrow(EmployeeNotFoundException::new);

        if (employeeUpdateDto == null) {
            throw new InvalidDataException("No values present");
        }
        if (employeeUpdateDto.name() != null) {
            employee.setName(employeeUpdateDto.name());
        }
        if (employeeUpdateDto.contractType() != null) {
            if (employeeUpdateDto.contractType().equalsIgnoreCase("CLT") || employeeUpdateDto.contractType().equalsIgnoreCase("ESTAGIO")) {
                employee.setContractType(ContractType.valueOf(employeeUpdateDto.contractType().toUpperCase()));
            } else {
                throw new InvalidDataException("Select a valid contract type (CLT/ESTAGIO)");
            }
        }
        repository.save(employee);
    }

    public void deleteEmployeeById(Long employeeId) {
        try {
            if (repository.existsById(employeeId)) repository.deleteById(employeeId);
            else {
                throw new EmployeeNotFoundException();
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("The employee cannot be deleted because they have associated workday records. Employee ID: " + employeeId);
        }
    }

}
