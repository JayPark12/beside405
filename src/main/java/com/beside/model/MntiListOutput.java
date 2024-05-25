package com.beside.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MntiListOutput {

    private String mnti_list_no ;
    private String mnti_name;    //산이름
    private String mnti_reb;     //산난이도
    private String poto_url;     //사진 api url
    private String mnti_add;

}

