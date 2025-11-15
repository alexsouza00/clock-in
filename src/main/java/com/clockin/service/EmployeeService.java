package com.clockin.service;

import com.clockin.exceptions.DataBaseException;
import com.clockin.exceptions.EmployeeNotFoundException;
import com.clockin.model.Employee;
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
        } else
            throw new EmployeeNotFoundException();
    }

    public void registerEmployee(Employee employee) {
        repository.save(employee);
    }

    public void deleteEmployeeById(Long employeeId) {
        try {
            if(repository.existsById(employeeId))
            repository.deleteById(employeeId);
            else {
                throw new EmployeeNotFoundException();
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("The employee cannot be deleted because they have associated workday records. Employee ID: " + employeeId);
        }
    }
}
