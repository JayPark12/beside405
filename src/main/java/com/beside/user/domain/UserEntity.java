package com.beside.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name="USER_INFO") @Builder//JPA
@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper=false) //lombok
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "NICKNAME", nullable = false)
    private String nickname;

    @Column(name = "CALL_NO")
    private String callNo;

    @Column(name = "USER_STS") // 0 : 정상 유저, 1: 카카오유저 : 휴면 유저, 2 : 정지 , 3 : 신고유저, 4 :신고유저 9 : 어드민 유저
    private String userSts;

    @Column(name = "CREAT_DT")
    private LocalDateTime creatDt;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "del_yn")
    private String delYn;

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void deleteUser() {
        this.delYn = "N";
    }
}
