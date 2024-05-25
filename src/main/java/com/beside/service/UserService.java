package com.beside.service;

import com.beside.DAO.UserDao;
import com.beside.Entity.UserEntity;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public String login(UserEntity userInput) {
        String id = userInput.getId();
        String password = userInput.getPassword();


        UserEntity Byid = userDao.findUser(id);

        // 비밀번호 일치 여부 확인
        if(StringUtils.equals(password, Byid.getPassword())){

            // JWT 토큰 반환
            String jwtToken = jwtProvider.generateJwtToken(Byid.getId());

            return jwtToken;
        }

        return "로그인 실패";
    }
}
