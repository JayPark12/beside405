package com.beside.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.beside.exception.InvalidToken;
import com.beside.user.domain.UserEntity;
import com.beside.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtProvider  {

    @Autowired
    private UserRepository userRepository;

    private final Key SECRET_KEY;

    @Value("${jwt.secret}")
    private String secretKey;

    static Long EXPIRE_TIME = 60L * 60L * 1000L; // 만료 시간 1시간


    private Algorithm getSign(){
        return Algorithm.HMAC512(secretKey);
    }

    public JwtProvider (@Value("${jwt.secret}") String key) {
        SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }


    // Jwt 토큰 생성
    public String generateJwtToken(String id){
        Date tokenExpiration = new Date(System.currentTimeMillis() + (EXPIRE_TIME));
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




    private Jws<Claims> getClaims (String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }

    public boolean validateToken(String token) {
        try {
            return getClaims(token).getBody().getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
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

    public String getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    public Authentication getAuthentication(String token) {
        String userId = getUserIdFromToken(token);
        UserDetails userDetails = new User(userId, "", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


}
