package com.beside.schedule.repository;

import com.beside.schedule.domain.ScheduleMemo;
import org.springdoc.core.providers.JavadocProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleMemoRepository extends JpaRepository<ScheduleMemo, String> {
    List<ScheduleMemo> findByScheduleId(String scheduleId);
}
