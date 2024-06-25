package com.beside.user.service;

import com.beside.jwt.JwtProvider;
import com.beside.user.domain.RandomNickname;
import com.beside.user.domain.UserEntity;
import com.beside.user.dto.*;
import com.beside.user.exception.UserErrorInfo;
import com.beside.user.exception.UserException;
import com.beside.user.repository.RandomNicknameRepository;
import com.beside.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RandomNicknameRepository randomNicknameRepository;

    LocalDate localDate = LocalDate.now();

    @Transactional
    public SignUpResponse joinUser(SignUpRequest request) {


        if (userRepository.existsById(request.getUserId())) {
            throw new UserException(UserErrorInfo.ID_ALREADY_EXISTS);
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity user = UserEntity.builder().id(request.getUserId())
                .nickname(createNickname())
                .callNo(request.getCallNo())
                .userSts("0").creatDt(localDate)
                .password(hashedPassword).build();

        userRepository.save(user);
        return SignUpResponse.builder().userId(request.getUserId()).nickname(user.getNickname()).desc("계정이 생성 되었습니다.").build();
    }

    public SignUpResponse joinFromKakao(String userId) {
        return null;
    }



    //랜덤 닉네임 생성
    public String createNickname() {
        List<String> firstNameList = randomNicknameRepository.findByPart("first");
        List<String> secondNameList = randomNicknameRepository.findByPart("second");

        Collections.shuffle(firstNameList);
        Collections.shuffle(secondNameList);

        String firstName = firstNameList.get(0);
        String secondName = secondNameList.get(0);

        return firstName + " " + secondName;
    }



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

        response.setHeader("Authorization", "Bearer " + jwt); //jwt 응답 header에 추가

        return LoginResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .callNo(user.getCallNo())
                .token(jwt)
                .bearerToken("Bearer " + jwt)
                .build();
    }


    public UserInfoResponse userInfo(String userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorInfo.NOT_FOUND_USER));
        return UserInfoResponse.builder()
                .userId(userId)
                .nickname(user.getNickname())
                .callNo(user.getCallNo())
                .creatDt(user.getCreatDt())
                .build();
    }

    @Transactional
    public String updateNickname(String userId, String newNickname) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorInfo.NOT_FOUND_USER));
        user.updateNickname(newNickname);
        userRepository.save(user);
        return user.getNickname();
    }

    @Transactional
    public void updatePassword(String userId, UpdatePasswordRequest request) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorInfo.NOT_FOUND_USER));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new UserException(UserErrorInfo.INCORRECT_OLD_PASSWORD);
        }
        if (!Objects.equals(request.getNewPassword(), request.getConfirmPassword())) {
            throw new UserException(UserErrorInfo.PASSWORD_CONFIRMATION_MISMATCH);
        }
        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public String calResult(MbtiDto mbtiDto) {
        return null;
    }
}
