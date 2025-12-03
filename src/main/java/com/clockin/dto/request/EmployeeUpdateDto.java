package com.clockin.dto.request;

import com.clockin.model.enums.ContractType;
import jakarta.validation.constraints.Pattern;


public record EmployeeUpdateDto(String name, String contractType) {
}
