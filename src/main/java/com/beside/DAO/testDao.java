package com.beside.DAO;
import com.beside.Entity.testEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface testDao extends Repository<testEntity, String> {

    @Query(value =
            """
               select aaaaa
               FROM aaaa     
                    """,
            nativeQuery = true)
    testEntity aaaaSelect(@Param("aaaa")String aaaaa);

}
