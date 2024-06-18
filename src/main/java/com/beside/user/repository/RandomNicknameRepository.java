package com.beside.user.repository;

import com.beside.user.domain.RandomNickname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RandomNicknameRepository extends JpaRepository<RandomNickname, String> {
    @Query(nativeQuery = true, value = "select name from RANDOM_NICKNAME rn where part = :part")
    List<String> findByPart(String part);
}
