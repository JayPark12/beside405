package com.beside.reservation.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MntiReserListOutput {

    private String mntiListNo;
    private String mntiName;    //산이름
    private String mntiLevel;     //산난이도
    private String potoFile = null;     //사진 api url

}

