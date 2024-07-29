package com.beside.schedule.domain;

import jakarta.persistence.*;
import lombok.*;

import java.lang.reflect.Member;

@Getter
@Builder
@Table(name = "SCHEDULE_MEMBER")
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleMember {
    @EmbeddedId
    private MemberId id;
}
