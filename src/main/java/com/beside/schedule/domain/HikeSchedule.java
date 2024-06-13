package com.beside.schedule.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@Table(name = "hike_schedule")
@Entity
@NoArgsConstructor @AllArgsConstructor
public class HikeSchedule {
    @Id
    private int scheduleId;
    private String userId;
    private String scheduleName;
    private LocalDateTime scheduleDate;
    private String mountain;
    private int memberCount;
    private LocalDateTime createDate;
    private String delYn;
}
