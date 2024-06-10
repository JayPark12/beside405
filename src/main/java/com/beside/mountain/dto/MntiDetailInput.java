package com.beside.mountain.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MntiDetailInput {
    private String jwtToken;     //토큰값 (등록페이지로 갈때만 필요)
    private String mnti_name;    //산이름
    private String mnti_list_no ; //산코드
    private String mnti_add;     //산소재지
}
