package com.beside.reservation.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false) //lombok
public class MountainResrPk {
    @Id
    @Column(name = "MNTI_CNT", nullable = false)
    private int mntiCnt = 0;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Id
    @Column(name = "MNTI_LIST_NO", nullable = false)
    private String mntiListNo;
}
