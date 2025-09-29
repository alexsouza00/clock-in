package com.clockin.controller;

import com.clockin.model.Employee;
import com.clockin.service.EmployeeService;
import com.clockin.service.WorkdayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private  EmployeeService employeeService;
    private  WorkdayService workdayService;

    @GetMapping
    public ResponseEntity<List<Employee>> listEmployees(){

       return ResponseEntity.ok().body(employeeService.getEmployees());

    }

    @GetMapping("/{employeeId}/workday")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long employeeId){

        return ResponseEntity.ok().body(employeeService.getEmployeeById(employeeId));

    }

    @PostMapping("/{employeeId/workday}")
    public void clockIn(@PathVariable Long employeeId){

    }
}
