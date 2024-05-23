package com.beside.repository;

import com.beside.Entity.testEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface testRepository extends JpaRepository<testEntity, String> {

}
