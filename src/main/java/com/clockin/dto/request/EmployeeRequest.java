package com.clockin.dto.request;

import com.clockin.model.enums.ContractType;
import jakarta.validation.constraints.*;

public record EmployeeRequest(@Size(min = 3, max = 60) String name,
                              @NotNull ContractType contractType) {
}
