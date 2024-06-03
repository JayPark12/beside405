package com.beside.service;

import com.beside.Entity.UserEntity;
import com.beside.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public String login(UserEntity userInput) {
        String id = userInput.getId();
        String password = userInput.getPassword();


        UserEntity Byid = userRepository.findUser(id);

        if(StringUtils.equals(Byid.getUserSts(), "0") || StringUtils.equals(Byid.getUserSts(), "9"))

        // 비밀번호 일치 여부 확인
        if(passwordEncoder.matches(password, Byid.getPassword())){

            // JWT 토큰 반환
            String jwtToken = jwtProvider.generateJwtToken(Byid.getId());

            return jwtToken;
        }

        return "loginFalse"; //프론트에 넘기고 창뜨게 하기
    }
    @Transactional
    public UserEntity joinUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setUserSts("0");
        if(userEntity.getCreatDt() ==null){
            userEntity.setCreatDt(LocalDate.now());
        }

        if (userRepository.existsById(userEntity.getId())) {
            throw new IllegalArgumentException("이미 사용 중인 ID입니다.");
        }

        return userRepository.save(userEntity);
    }
}
