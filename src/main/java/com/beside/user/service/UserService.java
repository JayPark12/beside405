package com.beside.user.service;

import com.beside.jwt.JwtProvider;
import com.beside.user.domain.UserEntity;
import com.beside.user.dto.*;
import com.beside.user.exception.UserErrorInfo;
import com.beside.user.exception.UserException;
import com.beside.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public LoginResponse login(UserInput userInput, HttpServletResponse response) {
        String id = userInput.getId();
        String password = userInput.getPassword();

        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserException(UserErrorInfo.NOT_FOUND_USER));

        if (!StringUtils.equals(user.getUserSts(), "0") && !StringUtils.equals(user.getUserSts(), "9")) {
            throw new UserException(UserErrorInfo.UNAVAILABLE_USER);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException(UserErrorInfo.PASSWORD_MISMATCH);
        }

        String jwt = jwtProvider.generateJwtToken(id);

        // JWT를 응답 헤더에 추가
        response.setHeader("Authorization", "Bearer " + jwt);

        return LoginResponse.builder()
                .userId(user.getId())
                .nickName(user.getNickName())
                .callNo(user.getCallNo())
                .token(jwt).build();
    }

    @Transactional
    public SignUpResponse joinUser(SignUpRequest request) {
        LocalDate localDate = LocalDate.now();
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity user = UserEntity.builder().id(request.getUserId())
                .nickName(request.getNickName())
                .callNo(request.getCallNo())
                .userSts("0").creatDt(localDate)
                .password(hashedPassword).build();

        userRepository.save(user);
        return SignUpResponse.builder().userId(request.getUserId()).desc("계정이 생성 되었습니다.").build();
    }

    public List<UserListResponse> getUserList(String userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorInfo.NOT_FOUND_USER));
        if(!userEntity.getUserSts().equals("9")) {
            throw new UserException(UserErrorInfo.NOT_HAVE_PERMISSION);
        }
        return userRepository.findAll().stream()
                .map(user -> new UserListResponse(user.getId(), user.getNickName(), user.getCallNo()))
                .collect(Collectors.toList());
    }
}
