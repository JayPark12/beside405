package com.beside.schedule.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Table(name = "schedule_member")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleMember {
    @EmbeddedId
    private ScheduleMemberId scheduleMemberId;
}
