package com.beside.schedule.repository;

import com.beside.schedule.domain.HikeSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface HikeScheduleRepository extends JpaRepository<HikeSchedule, String> {
    List<HikeSchedule> findByUserId(String userId);

    Optional<HikeSchedule> findByUserIdAndDelYn(String userId, String delYn);

    Optional<HikeSchedule> findByUserIdAndScheduleId(String userId, String scheduleId);

    Optional<HikeSchedule> findByScheduleId(String scheduleId);
}
