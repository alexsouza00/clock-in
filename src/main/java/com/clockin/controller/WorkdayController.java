package com.clockin.controller;

import com.clockin.dto.response.WorkStats;
import com.clockin.model.Workday;
import com.clockin.service.WorkdayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workdays")
public class WorkdayController {

    private final WorkdayService workdayService;

    public WorkdayController(WorkdayService workdayService) {
        this.workdayService = workdayService;
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<List<Workday>> getAllWorkdaysByEmployee(@PathVariable Long employeeId){
            return ResponseEntity.ok().body(workdayService.getAllWorkdaysByEmployee(employeeId));
    }

    @PostMapping("/{employeeId}")
    public ResponseEntity<String> registerWorkday(@PathVariable Long employeeId) {
        workdayService.registerWorkday(employeeId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Recorded time!");
    }

}
