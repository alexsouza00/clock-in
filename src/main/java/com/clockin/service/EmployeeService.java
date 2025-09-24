package com.clockin.service;

import com.clockin.model.Employee;
import com.clockin.model.enums.ContractType;
import com.clockin.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getEmployees(){
        Employee emp = new Employee("Alex", "123123", ContractType.CLT);
        Employee em2 = new Employee("Alex", "123123", ContractType.CLT);
        repository.save(emp);
        repository.save(em2);

        List<Employee> employees = repository.findAll();
        return employees;

    }
}
