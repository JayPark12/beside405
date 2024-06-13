package com.beside.mountain.repository;

import com.beside.mountain.domain.MntiReserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ReserRepository extends CrudRepository<MntiReserEntity, String> {

    @Query(value = """
    SELECT MAX(MNTI_CNT) FROM MOUNTAIN_RESER
    WHERE  id =:id
    AND   MNTI_LIST_NO =:mntilist_no
            """
            , nativeQuery = true)
    Integer findByMntiReserSerch(@Param("id")String id, @Param("mntilist_no")String mntilist_no
    );
}
