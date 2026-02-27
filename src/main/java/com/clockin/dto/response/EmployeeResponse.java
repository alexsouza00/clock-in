package com.clockin.dto.response;

import com.clockin.model.enums.ContractType;

public record EmployeeResponse(long id, String name, ContractType contractType) {
}
