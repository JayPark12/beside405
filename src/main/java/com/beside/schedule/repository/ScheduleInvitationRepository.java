package com.beside.schedule.repository;

import com.beside.schedule.domain.ScheduleInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleInvitationRepository extends JpaRepository<ScheduleInvitation, String> {
    Optional<ScheduleInvitation> findByInvitationId(String invitationId);
}
