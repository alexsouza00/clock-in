package com.clockin.controller;

import com.clockin.dto.WorkStatistic;
import com.clockin.model.Employee;
import com.clockin.service.EmployeeService;
import com.clockin.service.WorkdayService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> listEmployees() {
        return ResponseEntity.ok().body(employeeService.getEmployees());
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long employeeId) {
        return ResponseEntity.ok().body(employeeService.getEmployeeById(employeeId));
    }

    @PostMapping()
    public ResponseEntity registerEmployee(@RequestBody @Valid Employee employee) {
        employeeService.registerEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body("test");
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity deleteEmployeeById(@PathVariable Long employeeId) {
        employeeService.deleteEmployeeById(employeeId);
        return ResponseEntity.status(HttpStatus.OK).body("test");
    }
}
