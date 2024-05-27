package com.beside.DAO;

import com.beside.Entity.MntiEntity;
import com.beside.Entity.MntiReserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MntiDao extends Repository<MntiEntity,String> {

    @Query(value = """
    SELECT * FROM MOUNTAIN_INFO """
    , nativeQuery = true)
    List<MntiEntity> findByMnti();


    @Query(value = """
    SELECT * FROM MOUNTAIN_INFO
    WHERE  MNTI_NAME LIKE %:mntiName%
            """
            , nativeQuery = true)
    List<MntiEntity> findByMntiSerch(@Param("mntiName")String mntiName
    );

    @Query(value = """
    SELECT MAX(MNTI_CNT) FROM MOUNTAIN_RESER
    WHERE  id =: id
    AND   MNTILIST_NO =:
            """
            , nativeQuery = true)
    int findByMntiReserSerch(@Param("id")String mntiName, @Param("mntilistno")String mntilistno
    );


}
