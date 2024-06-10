package com.beside.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.beside.user.domain.UserEntity;
import com.beside.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider  {

    @Autowired
    private UserRepository userRepository;

    private final Key SECRET_KEY;
    private final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    private static final String AUTHORITIES_KEY = "auth";

    @Value("${jwt.secret}")
    private String secretKey;

    static Long EXPIRE_TIME = 60L * 60L * 1000L; // 만료 시간 1시간


    private Algorithm getSign(){
        return Algorithm.HMAC512(secretKey);
    }

    public JwtProvider (@Value("${jwt.secret}") String key) {
        SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

//    //객체 초기화, secretKey를 Base64로 인코딩한다.
//    @PostConstruct
//    protected void init() {
//        this.secretKey = Base64.getEncoder().encodeToString(this.secretKey.getBytes());
//    }

    // Jwt 토큰 생성
    public String generateJwtToken(String id){
        Date tokenExpiration = new Date(System.currentTimeMillis() + (EXPIRE_TIME));

//        return JWT.create()
//                .withSubject(id) //토큰 이름
//                .withExpiresAt(tokenExpiration)
//                .withClaim("id", id)
//                .sign(this.getSign());

        return Jwts.builder()
                .setSubject(id)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .setExpiration(tokenExpiration)
                .compact();
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


    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
