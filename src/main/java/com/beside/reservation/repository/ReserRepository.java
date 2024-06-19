package com.beside.reservation.repository;

import com.beside.reservation.domain.MntiReserEntity;
import com.beside.reservation.dto.MntiReserListOutput;
import com.beside.reservation.dto.MntiReserOutput;
import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReserRepository extends JpaRepository<MntiReserEntity, String> {

    @Query(value = """
    SELECT MAX(MNTI_CNT) 
    FROM MOUNTAIN_RESER
    WHERE  id =:id
    AND   MNTI_LIST_NO =:mntiListNo
            """
            , nativeQuery = true)
    Integer findByMntiReserSerch(@Param("id")String id, @Param("mntiListNo")String mntiListNo
    );

    @Query(value = """
    SELECT MNTI_STS
    FROM MOUNTAIN_RESER
    WHERE  id =:id
    AND   MNTI_LIST_NO =:mntiListNo
    AND   MNTI_COURSE  = :mntiCouse
    AND   MNTI_STR_DATE = :mntiStrDate
            """
            , nativeQuery = true)
    String findByMntiReserSerchForInputCheck(@Param("id")String id,
                                              @Param("mntiListNo")String mntiListNo,
                                              @Param("mntiCouse")String mntiCouse,
                                              @Param("mntiStrDate")LocalDate mntiStrDate);

    //날짜 지난것에 대한 sts 변경 batch
    @Transactional
    @Modifying
    @Query(value = """
    UPDATE MOUNTAIN_RESER 
    SET MNTI_STS = :mntiSts 
    WHERE MNTI_STR_DATE < :mntiStrDate"""
            , nativeQuery = true)
    void updateStatusForOlderDates(@Param("mntiStrDate") LocalDate mntiStrDate,
                                   @Param("mntiSts") String mntiSts);


    @Query(value = """
    SELECT *
    FROM MOUNTAIN_RESER
    WHERE id =:id
    AND   MNTI_STS  = :mntiSts
            """
            , nativeQuery = true)
    List<MntiReserEntity> findByMntiCntAndUserIdAndScheduleId(@Param("id")String id,
                                                                  @Param("mntiSts")String mntiSts);

    @Query(value = """
    SELECT *
    FROM MOUNTAIN_RESER
    WHERE id =:id
    AND   MNTI_LIST_NO  = :mntiListNo
    AND   MNTI_STR_DT   = :mntiStrDt
            """
            , nativeQuery = true)
    MntiReserEntity findByIdAndMntiListNoAndMntiStrDt(@Param("id")String id,
                                                            @Param("mntiListNo")String mntiListNo,
                                                            @Param("mntiStrDt")LocalDate mntiStrDt);



}
