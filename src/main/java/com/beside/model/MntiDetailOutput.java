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

    private String mntiName;    //산이름
    private String mntiReb;     //산난이도
    private String mntiHigh;    //고도
    private String mntiAdd;     //산 소제지

    private List<Course> course;
    private List<String> potoUrl;
}

