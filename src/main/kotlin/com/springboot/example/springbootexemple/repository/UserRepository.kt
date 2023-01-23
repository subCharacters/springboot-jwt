package com.springboot.example.springbootexemple.repository

import com.springboot.example.springbootexemple.entity.Members
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<Members?, Long> {
    // lazy조회가 아닌 eager조회로 가져옴
    @EntityGraph(attributePaths = ["authorities"])
    fun findOneWithAuthoritiesByUsername(username: String): Optional<Members>
}