package com.clockin.controller;

import com.clockin.dto.request.EmployeeCreateDto;
import com.clockin.dto.request.EmployeeUpdateDto;
import com.clockin.dto.response.EmployeeResponseDto;
import com.clockin.dto.response.WorkStats;
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
    private final WorkdayService workdayService;

    public EmployeeController(EmployeeService employeeService, WorkdayService workdayService) {
        this.employeeService = employeeService;
        this.workdayService = workdayService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployees() {
        return ResponseEntity.ok().body(employeeService.getAllEmployees());
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long employeeId) {
        return ResponseEntity.ok().body(employeeService.getEmployeeByIdController(employeeId));
    }

    @GetMapping("/stats/{employeeId}")
    public ResponseEntity<WorkStats> getWorkStatsByEmployeeId(@PathVariable Long employeeId) {
        WorkStats workStats = workdayService.getWorkStatsByEmployee(employeeId);
        return ResponseEntity.ok().body(workStats);
    }

    @PostMapping()
    public ResponseEntity<String> registerEmployee(@RequestBody @Valid EmployeeCreateDto employee) {
        employeeService.registerEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body("\n" + "Employee successfully registered!");
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long employeeId, @Valid @RequestBody EmployeeUpdateDto employeeUpdateDto){
        employeeService.updateEmployee(employeeId, employeeUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body("Employee Updated!");
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable Long employeeId) {
        employeeService.deleteEmployeeById(employeeId);
        return ResponseEntity.status(HttpStatus.OK).body("Employee deleted!");
    }
}
