package com.beside.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MntiReserInput {

    private String mntiName;    //산이름
    private String mntilistNo ; //산코드
    private String mntiAdd;     //산주소

}
