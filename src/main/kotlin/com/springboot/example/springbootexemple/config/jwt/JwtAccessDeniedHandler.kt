package com.springboot.example.springbootexemple.config.jwt

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

// 필요한 권한이 존재하지 않는 경우에 403에러를 반환하는 클래스
@Component
class JwtAccessDeniedHandler: AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        response?.sendError(HttpServletResponse.SC_FORBIDDEN)
    }
}