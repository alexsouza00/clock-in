package com.clockin.dto.request;

import jakarta.validation.constraints.*;

public record EmployeeDto(@Size(min = 3, max =  60) @NotNull @NotBlank String name, @NotNull @NotBlank String contractType) {
}
