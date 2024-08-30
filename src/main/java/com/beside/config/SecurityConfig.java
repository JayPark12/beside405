package com.beside.config;

import com.beside.common.handler.CustomLogoutHandler;
import com.beside.jwt.JwtFilter;
import com.beside.user.repository.UserRepository;
import com.beside.jwt.JwtProvider;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableSpringDataWebSupport
public class SecurityConfig {

    @Value("${spring.security.cors.allow.methods:1,2,3,4,5,6}")
    private String[] allowedMethods;

//    @Value("#{'${spring.security.origin}'.split(',')}")
//    private List<String> allowedOriginPaths;

    private List<String> allowedOriginPaths;

    private final String[] excludedEndPoints = {
            "/user/join",
            "/user/login",
            "/main/**",
            "/reser/**",
            "/detail/**",
            "/kakao/**",
            "/test/**",
            "/user/kakaoLogin",
            "/schedule/**"
    };

    @PostConstruct
    public void init() {
        allowedOriginPaths = Arrays.asList("http://localhost:5173", "https://dev-over-the-mountain.vercel.app", "https://over-the-mountain.vercel.app" ,"https://over-the-mountain.site", "https://www.over-the-mountain.site");
    }

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private CustomLogoutHandler customLogoutHandler;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/h2-console/**",
                "/favicon.ico",
                "/error",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/v3/api-docs/**");
    }

    // 시큐리티 필터 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // formLogin 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // httpBasic 비활성화
                // 동일 도메인에서는 iframe 접근 가능하도록 X-Frame-Options는 sameOrigin으로 설정
                .headers(headersConfig -> headersConfig
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests((authorizeRequest) -> {
                    authorizeRequest //HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정함
                            .requestMatchers(excludedEndPoints).permitAll() //허용할 api 지정
                            .anyRequest().authenticated(); //나머지 요청들에 대해서는 모두 인증을 받음
//                            .anyRequest().permitAll(); // 모든 요청에 대해 접근 허용
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(customLogoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
//                에러 핸들링 설정
                .exceptionHandling(handling -> handling
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
            //config.setAllowedMethods(List.of(SwaggerPatterns));
            config.setAllowedOrigins(allowedOriginPaths);
            config.setAllowedMethods(List.of(allowedMethods));
            config.setAllowedHeaders(List.of(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, HttpHeaders.SET_COOKIE, HttpHeaders.ACCEPT, HttpHeaders.ACCEPT_LANGUAGE, HttpHeaders.CONTENT_LANGUAGE, "Sec-WebSocket-Version", "Sec-WebSocket-Key"));
            config.setAllowCredentials(true);
            config.setMaxAge(3600L);
            return config;
        };
    }

    // 인증 실패 경우 에러 응답 핸들링
    private final AuthenticationEntryPoint authenticationEntryPoint = ((request, response, authException) -> {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    });

    // 권한 처리에 대한 에러 응답 핸들링
    private final AccessDeniedHandler accessDeniedHandler = ((request, response, accessDeniedException) -> {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    });



}
