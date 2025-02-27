package com.project.security.jwt.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api")
public class TestController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint!";
    }

    @GetMapping("/user")
    public String userEndpoint() {
        return "Hello, USER! You are authorized.";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Hello, ADMIN! You have full access.";
    }
}
