package com.beside.config;

import com.beside.user.repository.UserRepository;
import com.beside.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableSpringDataWebSupport
public class SecurityConfig {
    private final UserRepository userRepository;

    @Value("${spring.security.cors.allow.methods:1,2,3,4,5,6}")
    private String[] allowedMethods;

    private static final String[] SwaggerPatterns = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    };

    @Bean
    public JwtProvider jwtTokenProvider() {
        return new JwtProvider(userRepository);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 필터 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests((authorizeRequest) -> {
                    authorizeRequest //HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정함
                            //.requestMatchers("/test").permitAll() //허용할 api 지정
                            //.anyRequest().authenticated(); //나머지 요청들에 대해서는 모두 인증을 받음
                            .anyRequest().permitAll(); // 모든 요청에 대해 접근 허용
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //에러 핸들링 설정
                .exceptionHandling(handling ->
                handling
                        //인증 실패 혹은 인증 헤더에 없는 경우 401 응답
                        .authenticationEntryPoint(authenticationEntryPoint)
                        //권한에 대한 처리 에러 403 응답
                        .accessDeniedHandler(accessDeniedHandler));

        return http.build();
    }

    // CORS 설정 메서드
    private CorsConfigurationSource corsConfigurationSource(){
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedMethods(List.of(SwaggerPatterns));
            config.setAllowedMethods(List.of(allowedMethods));
            config.setAllowCredentials(true);
            config.setMaxAge(3600L);
            return config;
        };
    }

    // 권한 처리에 대한 에러 응답 핸들링
    private final AuthenticationEntryPoint authenticationEntryPoint = ((request, response, authException) -> {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    });

    // 인증 실패 경우 에러 응답 핸들링
    private final AccessDeniedHandler accessDeniedHandler = ((request, response, accessDeniedException) -> {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    });



}
