package com.beside.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name="RANDOM_NICKNAME")
@Entity
public class RandomNickname {
    @Id
    @Column(name = "part")
    private String part;

    @Column(name = "name")
    private String name;
}
