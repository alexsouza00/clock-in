package com.clockin.controller;

import com.clockin.dto.WorkStatistic;
import com.clockin.model.Employee;
import com.clockin.service.EmployeeService;
import com.clockin.service.WorkdayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final WorkdayService workdayService;

    public EmployeeController(EmployeeService employeeService, WorkdayService workdayService) {
        this.employeeService = employeeService;
        this.workdayService = workdayService;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> listEmployees() {

        return ResponseEntity.ok().body(employeeService.getEmployees());

    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long employeeId) {

        return ResponseEntity.ok().body(employeeService.getEmployeeById(employeeId));

    }

    @PostMapping("/{employeeId}/workday")
    public void registerWorkday(@PathVariable Long employeeId) {
        workdayService.registerWorkday(employeeId);
    }

    @GetMapping("/{employeeId}/workdays")
    public ResponseEntity<WorkStatistic> getWorkdays(@PathVariable Long employeeId) {
      WorkStatistic workdays = workdayService.workStatistic(employeeId);
        return ResponseEntity.ok().body(workdays);
    }
}
