package com.beside.user.repository;

import com.beside.user.domain.UserExitReasons;
import com.beside.user.dto.UserExitRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserExitReasonRepository extends JpaRepository<UserExitReasons, String> {
}
