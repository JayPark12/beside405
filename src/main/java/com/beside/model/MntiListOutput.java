package com.beside.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MntiListOutput {

    private String mntilistNo ;
    private String mntiName;    //산이름
    private String mntiReb;     //산난이도
    private String potoUrl;     //사진 api url
    private String mntiAdd;


}

