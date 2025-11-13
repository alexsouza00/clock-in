package com.clockin.service;

import com.clockin.model.Employee;
import com.clockin.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getEmployees() {
        List<Employee> employees = repository.findAll();
        return employees;
    }

    public Employee getEmployeeById(Long id) {
        Optional<Employee> ep = repository.findById(id);
        return ep.get();
    }

    public void registerEmployee(Employee employee){
        repository.save(employee);
    }

    public void deleteEmployeeById(Long employeeId){
        repository.deleteById(employeeId);
    }
}
