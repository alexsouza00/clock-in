package com.clockin.dto.request;


import com.clockin.dto.request.enums.WorkShift;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record WorkdayUpdateDto(

        @NotNull Long employeeId, @NotNull LocalDate workdayDate, @NotNull WorkShift workShift, @NotNull LocalTime newTime) {
}
