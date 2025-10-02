package com.clockin.repository;

import com.clockin.model.Workday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface WorkdayRepository extends JpaRepository<Workday, Long> {

    Optional<Workday> findByEmployeeIdAndWorkdayDate(Long employeeId, LocalDate workdayDate);

}
