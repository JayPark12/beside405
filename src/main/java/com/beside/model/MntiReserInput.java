package com.beside.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MntiReserInput {
    private String jwtToken ;    //토큰값
    private String mnti_name;    //산이름
    private String mnti_list_no ; //산코드
    private String mnti_add;     //산주소
    private String course_no;  //산코스번호

}
