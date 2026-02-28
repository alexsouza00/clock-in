package com.clockin.controller;

import com.clockin.dto.request.EmployeeRequest;
import com.clockin.dto.request.EmployeeUpdate;
import com.clockin.dto.response.EmployeeResponse;
import com.clockin.dto.response.WorkStats;
import com.clockin.service.EmployeeService;
import com.clockin.service.WorkdayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    @Operation(description = "Get all employees registered")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Return employees successfully"),
            @ApiResponse(responseCode = "404", description = "Employees not found")
    })
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity.ok().body(employeeService.getAllEmployees());
    }

    @Operation(description = "Get an Employee by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Return an Employee"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long employeeId) {
        return ResponseEntity.ok().body(employeeService.getEmployeeById(employeeId));
    }

    @Operation(description = "Get an employee's work statistics by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Return the statistics successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/stats/{employeeId}")
    public ResponseEntity<WorkStats> getWorkStatsByEmployeeId(@PathVariable Long employeeId) {
        WorkStats workStats = workdayService.getWorkStatsByEmployeeId(employeeId);
        return ResponseEntity.ok().body(workStats);
    }

    @Operation(description = "Register an Employee")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Register an Employee successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data for register an employee")
    })
    @PostMapping()
    public ResponseEntity<String> registerEmployee(@RequestBody @Valid EmployeeRequest employee) {
        employeeService.registerEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee registered successfully!");
    }

    @Operation(description = "Update Employee infos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Update successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not Found"),
            @ApiResponse(responseCode = "400", description = "Invalid data for employee update")
    })
    @PatchMapping("/{employeeId}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long employeeId, @Valid @RequestBody EmployeeUpdate employeeUpdate) {
        employeeService.updateEmployee(employeeId, employeeUpdate);
        return ResponseEntity.status(HttpStatus.OK).body("Employee updated!");
    }
}
