package com.beside.user.repository;

import com.beside.user.domain.RandomNickname;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RandomNicknameRepository extends JpaRepository<RandomNickname, String> {
    List<RandomNickname> findNameByPart(String part);
}
