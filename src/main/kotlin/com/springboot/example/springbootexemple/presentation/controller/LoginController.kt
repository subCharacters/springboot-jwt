package com.springboot.example.springbootexemple.presentation.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class LoginController {

    @GetMapping("/login")
    fun login(@RequestParam username: String, password: String): String {
        return ""
    }
}