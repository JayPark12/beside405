package com.beside.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MntiEntity {

    @Id
    private String mntilistNo;  //산번호
    private String mntiName;    //산이름
    private String mntiJson;    //코스 정보 Json파일명
    private String mntiCaution;  //주의사항
    private String mntiReb;


}
