package com.beside.mountain.repository;

import com.beside.mountain.domain.MntiEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MntiRepository extends Repository<MntiEntity,String> {

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
}
