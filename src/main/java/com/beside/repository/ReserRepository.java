package com.beside.repository;

import com.beside.Entity.MntiReserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ReserRepository extends CrudRepository<MntiReserEntity, String> {

    @Query(value = """
    SELECT MAX(MNTI_CNT) FROM MOUNTAIN_RESER
    WHERE  id =:id
    AND   MNTILIST_NO =:mntilist_no
            """
            , nativeQuery = true)
    Integer findByMntiReserSerch(@Param("id")String id, @Param("mntilist_no")String mntilist_no
    );


}
