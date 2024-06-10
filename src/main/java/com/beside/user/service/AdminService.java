package com.beside.user.service;

import com.beside.user.domain.UserEntity;
import com.beside.user.dto.UserListResponse;
import com.beside.user.exception.UserErrorInfo;
import com.beside.user.exception.UserException;
import com.beside.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public List<UserListResponse> getUserList(String userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorInfo.NOT_FOUND_USER));
        if(!userEntity.getUserSts().equals("9")) {
            throw new UserException(UserErrorInfo.NOT_HAVE_PERMISSION);
        }
        return userRepository.findAll().stream()
                .map(user -> new UserListResponse(user.getId(), user.getNickname(), user.getCallNo()))
                .collect(Collectors.toList());
    }

}
