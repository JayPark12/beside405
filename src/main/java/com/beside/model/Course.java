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
    private String course_no;
    private String course_name;
    private Long   mnti_time;
    private String   mnti_dist;    //등산로거리
    private String mnti_reb;
    private List<List<Coordinate>> paths;
}
