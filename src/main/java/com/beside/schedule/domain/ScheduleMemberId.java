package com.beside.schedule.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ScheduleMemberId implements Serializable {
    private int scheduleId;
    private String memberId;
}
