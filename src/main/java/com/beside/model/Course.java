package com.beside.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private String courseNo;
    private String courseName;
    private long mntiTime;
    private String mntiReb;
    private List<List<Coordinate>> paths; // 추가된 필드
}
