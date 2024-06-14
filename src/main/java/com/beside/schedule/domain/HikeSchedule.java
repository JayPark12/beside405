package com.beside.schedule.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Table(name = "HIKE_SCHEDULE")
@Entity @ToString
@NoArgsConstructor @AllArgsConstructor
public class HikeSchedule {
    @Id
    @Column(name = "schedule_id")
    private String scheduleId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "schedule_name")
    private String scheduleName;

    @Column(name = "schedule_date")
    private LocalDateTime scheduleDate;

    @Column(name = "mountain_id")
    private String mountainId;

    @Column(name = "member_count")
    private int memberCount;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "del_yn")
    private String delYn;
}