package com.beside.model;

import lombok.*;

import java.io.InputStream;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MntiDetailOutput {

    private String mnti_name;    //산이름
    private String mnti_reb;     //산난이도
    private String mnti_high;    //고도
    private String mnti_add;     //산 소제지

    private List<Course> course; //코스정보
    private List<String> poto_url; //사진 정보
    private List<Wather> wather_list;  //7일까지의 데이터
}

