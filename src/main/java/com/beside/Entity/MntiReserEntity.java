package com.beside.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MntiReserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mnticnt", nullable = false)
    private int mnticnt = 0;

    @Column(name = "mnticnt")
    private String id;

    @Column(name = "mntilist_no")
    private String mntilistNo;

    @Column(name = "mnti_caution")
    private String mntiCaution;

    @Column(name = "mnti_clim_tm")
    private String mntiClimTm;

    @Column(name = "mnti_cs")
    private String mntiCs;

    @Column(name = "mnti_cs_name")
    private String mntiCsName;

    @Column(name = "mnti_desc_tm")
    private String mntiDescTm;

    @Column(name = "mnti_dist")
    private String mntiDist;

    @Column(name = "mnti_end_tm")
    private String mntiEndTm;

    @Column(name = "mnti_fe")
    private String mntiFe;

    @Column(name = "mnti_json")
    private String mntiJson;

    @Column(name = "mnti_mt")
    private String mntiMt;

    @Column(name = "mnti_name")
    private String mntiName;

    @Column(name = "mnti_reb")
    private String mntiReb;

    @Column(name = "mnti_reser")
    private String mntiReser;

    @Column(name = "mnti_str_dt")
    private String mntiStrDt;

    @Column(name = "mnti_str_tm")
    private String mntiStrTm;

    @Column(name = "mnti_sts")
    private String mntiSts;



}
