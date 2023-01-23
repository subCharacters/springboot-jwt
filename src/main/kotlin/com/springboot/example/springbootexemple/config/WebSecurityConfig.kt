package com.springboot.example.springbootexemple.config

import com.springboot.example.springbootexemple.config.jwt.JwtAccessDeniedHandler
import com.springboot.example.springbootexemple.config.jwt.JwtAuthenticationEntryPoint
import com.springboot.example.springbootexemple.config.jwt.JwtSecurityConfig
import com.springboot.example.springbootexemple.config.jwt.TokenProvider
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


// WebSecurityConfigurerAdapter가 deprecated로 변경되어 빈 형식으로 설정하는 것으로 바뀜
@Configuration
@EnableWebSecurity // HttpSecurity를 못찾는 IDE버전 버그로 인해 넣어줌. 2022.2 버전 이후에선 IDE가 정상적으로 찾아줌.
@EnableMethodSecurity // @PreAuthorize 어노테이션을 메소드 단위로 추가하기 위해서 적용
class WebSecurityConfig(
    private var tokenProvider: TokenProvider,
    private var jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private var jwtAccessDeniedHandler: JwtAccessDeniedHandler
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    // 구버전에선 configure
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
         http
             .csrf().disable() // token을 사용하기 때문에 비활성화
             .exceptionHandling()
             .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 예외처리에 커스터마이징한 핸들러를 추가
             .accessDeniedHandler(jwtAccessDeniedHandler) // 예외처리에 커스터마이징한 핸들러를 추가
                 
             .and() // h2콘솔을 위한 설정 -- 여기부터
             .headers()
             .frameOptions()
             .sameOrigin() // h2콘솔을 위한 설정 -- 여기까지
             .and() // 세션을 사용하지 않기에 세션 설정을 STATELESS -- 여기부터
             .sessionManagement()
             .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않기에 세션 설정을 STATELESS -- 여기까지

             .and()
             .authorizeHttpRequests()
             //.antMatchers("/api/hello").permitAll()
             .antMatchers("/api/authenticate").permitAll() // 로그인 및 회원가입api는 토큰이 없는 상태에서 넘어 오기때문에 모두 열어주기
             .antMatchers("/api/signup").permitAll()
             .requestMatchers(PathRequest.toH2Console()).permitAll()
             .anyRequest().authenticated()

             .and()
             .apply(JwtSecurityConfig(tokenProvider))

        return http.build()
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer? {
        return WebSecurityCustomizer {
                web: WebSecurity -> web.ignoring()
            .antMatchers("/h2-console/**"
                , "/favicon.ico")
        }
    }
}


