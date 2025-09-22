package com.clockin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkdaySummary extends JpaRepository<WorkdaySummary, Long> {
}
