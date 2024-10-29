package com.beside.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name="USER_EXIT_REASONS")
@Entity
@ToString
public class UserExitReasons {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "exit_reason")
    private String exitReason;

    @Column(name = "exit_date")
    private LocalDateTime exitDate;
}
