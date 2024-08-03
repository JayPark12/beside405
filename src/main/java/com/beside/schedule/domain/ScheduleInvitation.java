package com.beside.schedule.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Builder
@Table(name = "SCHEDULE_INVITATION")
@Entity @ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleInvitation {
    @Id
    @Column(name = "invitation_id")
    private String invitationId;

    @Column(name = "schedule_id")
    private String scheduleId;

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "content")
    private String content;
}
