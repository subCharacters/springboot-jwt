package com.springboot.example.springbootexemple.config.jwt

import com.springboot.example.springbootexemple.entity.Members
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.stream.Collectors


@Component
class TokenProvider(
    @param:Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.token-validity-in-seconds}") tokenValidityInSeconds: Long
): InitializingBean {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private var key: Key? = null
    private val tokenValidityInMilliseconds: Long

    init {
        tokenValidityInMilliseconds = tokenValidityInSeconds * 1000
    }

    companion object {
        private const val authoritiesKey = "auth"
    }

    override fun afterPropertiesSet() {
        var keyBytes: ByteArray? = Decoders.BASE64.decode(secret)
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun createToken(authentication: Authentication): String {
        var authorities = authentication.authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","))

        val now: Long = Date().time
        val validity = Date(now + tokenValidityInMilliseconds)
        return Jwts.builder()
            .setSubject(authentication.name)
            .claim(authoritiesKey, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact()
    }

    fun getAuthentication(token: String?): Authentication {
        var claims = Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        val authorities: Collection<GrantedAuthority?> =
            Arrays.stream(claims[authoritiesKey].toString().split(",").toTypedArray())
                .map { role: String? -> SimpleGrantedAuthority(role) }
                .collect(Collectors.toList())

        val principal = Members(claims.subject, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: SecurityException) {
            logger.info("????????? JWT ???????????????.")
        } catch (e: MalformedJwtException) {
            logger.info("????????? JWT ???????????????.")
        } catch (e: ExpiredJwtException) {
            logger.info("????????? JWT ???????????????.")
        } catch (e: UnsupportedJwtException) {
            logger.info("???????????? ?????? JWT ???????????????.")
        } catch (e: IllegalArgumentException) {
            logger.info("JWT ????????? ?????????????????????.")
        }
        return false
    }
}