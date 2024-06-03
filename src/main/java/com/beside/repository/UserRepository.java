package com.beside.repository;

import com.beside.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    @Query(value = """
    SELECT * FROM USER_INFO WHERE id = :id"""
    , nativeQuery = true)
    UserEntity findUser(@Param("id") String id);

}
