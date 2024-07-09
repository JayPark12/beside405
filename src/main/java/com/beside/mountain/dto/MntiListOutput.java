package com.beside.mountain.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MntiListOutput {

    private String mntiListNo;
    private String mntiName;    //산이름
    private String mntiLevel;     //산난이도
    private String potoFile = null;     //사진 api url
    private String mntiAdd;
    private BigDecimal height;

}

