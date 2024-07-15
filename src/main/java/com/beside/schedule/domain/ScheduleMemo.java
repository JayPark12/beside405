package com.beside.schedule.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Table(name = "SCHEDULE_MEMO")
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleMemo {
    @Id
    @Column(name = "memo_id")
    private String memoId;

    @Column(name = "schedule_id")
    private String scheduleId;

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "content")
    private String content;

    @Column(name = "check_status")
    private boolean checkStatus;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    public void updateMemo(String content) {
        this.content = content;
    }

    public void updateCheckStatus(boolean checkStatus) {
        this.checkStatus = checkStatus;
    }
}
