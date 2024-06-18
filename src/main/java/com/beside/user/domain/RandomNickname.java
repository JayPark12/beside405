package com.beside.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name="RANDOM_NICKNAME")
@Entity @ToString
public class RandomNickname {
    @Id
    @Column(name = "part")
    private String part;

    @Column(name = "name")
    private String name;
}
