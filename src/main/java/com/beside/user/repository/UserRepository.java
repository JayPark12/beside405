package com.beside.user.repository;

import com.beside.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    @Query(value = """
    SELECT * FROM USER_INFO WHERE id = :id"""
    , nativeQuery = true)
    UserEntity findUser(@Param("id") String id);

    Optional<UserEntity> findByIdAndDelYn(String id, String delYn);

    Optional<UserEntity> findByIdContaining(String userId);

    Optional<UserEntity> findByIdContainingAndDelYn(String userId, String delYn);

    Optional<UserEntity> findFirstByIdContainingOrderByCreatDtDesc(String userId);

    Optional<UserEntity> findFirstByIdContainingAndDelYnOrderByCreatDtDesc(String userId, String delYn);
}
