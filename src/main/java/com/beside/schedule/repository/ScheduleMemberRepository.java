package com.beside.schedule.repository;

import com.beside.schedule.domain.MemberId;
import com.beside.schedule.domain.ScheduleMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleMemberRepository extends JpaRepository<ScheduleMember, MemberId> {
    List<ScheduleMember> findByIdMemberId(String userId);
}
