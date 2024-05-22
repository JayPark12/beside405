package com.beside.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class UserEntity {

    @Id
    private String id;
    private String nickName;
    private String callNo;
    private String userSts;
    private LocalDate creatDt;
    private String password;

}
