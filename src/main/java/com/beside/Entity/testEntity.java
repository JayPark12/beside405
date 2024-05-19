package com.beside.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class testEntity {

    @Id
    @GeneratedValue
    private String testData1 ;

}
