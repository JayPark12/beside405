package com.beside.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity @Table(name="USER_INFO") //JPA
@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper=false) //lombokk
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "NICK_NAME", nullable = false)
    private String nickName;

    @Column(name = "CALL_NO", nullable = false)
    private String callNo;

    @Column(name = "USER_STS") // 0 : 정상 유저, 1 : 휴면 유저, 2 : 정지 ,3 : 신고유저, 9 : 어드민 유저
    private String userSts;

    @Column(name = "CREAT_DT")
    private LocalDate creatDt;

    @Column(name = "password", nullable = false)
    private String password;
}
