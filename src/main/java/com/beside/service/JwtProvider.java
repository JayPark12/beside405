package com.beside.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.beside.Entity.UserEntity;
import com.beside.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
public class JwtProvider {
    private final UserRepository userRepository;
    static Long EXPIRE_TIME = 60L * 60L * 1000L; // 만료 시간 1시간

    @Value("${jwt.secret}")
    private String secretKey;


    private Algorithm getSign(){
        return Algorithm.HMAC512(secretKey);
    }
    //객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        this.secretKey = Base64.getEncoder().encodeToString(this.secretKey.getBytes());
    }


    // Jwt 토큰 생성
    public String generateJwtToken(String id){

        Date tokenExpiration = new Date(System.currentTimeMillis() + (EXPIRE_TIME));


        String jwtToken = JWT.create()
                .withSubject(id) //토큰 이름
                .withExpiresAt(tokenExpiration)
                .withClaim("id", id)
                .sign(this.getSign());

        return jwtToken;
    }

    /**
     * 토큰 검증
     *  - 토큰에서 가져온 id 정보와 DB의 유저 정보 일치하는지 확인
     *  - 토큰 만료 시간이 지났는지 확인
     * @param jwtToken
     * @return 유저 객체 반환
     */
    public UserEntity validToken(String jwtToken){
        try {

            String id = JWT.require(this.getSign())
                    .build().verify(jwtToken).getClaim("id").asString();

            // 비어있는 값이다.
            if (id == null){
                return null;
            }

            // EXPIRE_TIME이 지나지 않았는지 확인
            Date expiresAt = JWT.require(this.getSign()).acceptExpiresAt(EXPIRE_TIME).build().verify(jwtToken).getExpiresAt();
            if (!this.validExpiredTime(expiresAt)) {
                // 만료시간이 지났다.
                return null;
            }

            UserEntity tokenUser = userRepository.findUser(id);

            return tokenUser;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    // 만료 시간 검증
    private boolean validExpiredTime(Date expiresAt){
        // LocalDateTime으로 만료시간 변경
        LocalDateTime localTimeExpired = expiresAt.toInstant().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();

        // 현재 시간이 만료시간의 이전이다
        return LocalDateTime.now().isBefore(localTimeExpired);

    }
}
