package com.springboot.example.springbootexemple.config.jwt

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class JwtSecurityConfig(
    private val tokenProvider: TokenProvider
): SecurityConfigurerAdapter<DefaultSecurityFilterChain?, HttpSecurity>() {

    // 세큐리티 로직에 필터를 등록
    override fun configure(builder: HttpSecurity?) {
        builder?.addFilterBefore(
            JwtFilter(tokenProvider),
            UsernamePasswordAuthenticationFilter::class.java
        )
    }
}