package com.beside.mountain.dto;


import com.beside.util.Coordinate;
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
    private Long mntiTime;
    private String mntiDist;    //등산로거리
    private String mntiLevel;
    private List<List<Coordinate>> paths;
}
