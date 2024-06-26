package com.beside.mountain.repository;

import com.beside.mountain.domain.MntiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MntiRepository extends JpaRepository<MntiEntity,String> {

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
    SELECT * FROM MOUNTAIN_INFO
    WHERE  MNTI_LIST_NO = :mntiListNo
            """
            , nativeQuery = true)
    MntiEntity findByMntiInfo(@Param("mntiListNo")String mntiListNo
    );


    Optional<MntiEntity> findByMntiListNo(String mountainId);

    List<MntiEntity> findByMntiNameContaining(String keyword);
}
