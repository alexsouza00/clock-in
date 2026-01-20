package com.clockin.dto.response;

import com.clockin.model.Employee;

public class EmployeeResponseDto {
    private long id;
    private String name;
    private String contractType;

    public EmployeeResponseDto(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.contractType = employee.getContractType().toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }
}
