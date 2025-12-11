package com.clockin.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record WorkdayDto(

        @NotNull Long employeeId, @NotNull LocalDate workdayDate, @NotNull String shift, @NotNull LocalTime newTime) {
}
