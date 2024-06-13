package com.beside.schedule.repository;

import com.beside.schedule.domain.HikeSchedule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HikeScheduleRepository extends CrudRepository<HikeSchedule, Integer> {
}
