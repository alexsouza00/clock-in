package com.clockin.controller;

import com.clockin.dto.WorkStatistic;
import com.clockin.model.Workday;
import com.clockin.service.WorkdayService;
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
    public ResponseEntity<List<Workday>> getAllWorkdays(@PathVariable Long employeeId){
            return ResponseEntity.ok().body(workdayService.getAllWorkdays(employeeId));
    }

    @GetMapping("/{employeeId}/statistics")
    public ResponseEntity<WorkStatistic> getWorkdays(@PathVariable Long employeeId) {
        WorkStatistic workdays = workdayService.workStatistic(employeeId);
        return ResponseEntity.ok().body(workdays);
    }

    @PostMapping("/{employeeId}")
    public void registerWorkday(@PathVariable Long employeeId) {
        workdayService.registerWorkday(employeeId);
    }

}
