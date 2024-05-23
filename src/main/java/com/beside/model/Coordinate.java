package com.beside.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinate {
    private double x;
    private double y;

    public Coordinate(double[] coordinates) {
        if (coordinates != null && coordinates.length == 2) {
            this.x = coordinates[0];
            this.y = coordinates[1];
        }
    }
}
