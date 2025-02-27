package com.project.security.jwt.controller;

import com.project.security.jwt.data.entity.CustomUser;
import com.project.security.jwt.data.repository.UserRepository;
import com.project.security.jwt.mapper.UserMapper;
import com.project.security.jwt.model.AuthenticationResponse;
import com.project.security.jwt.model.LoginRequest;
import com.project.security.jwt.model.TokenRefreshRequest;
import com.project.security.jwt.model.UserRegisterModel;
import com.project.security.jwt.service.CustomUserServiceDetails;
import com.project.security.jwt.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JWTUtil jwtUtil;
//    @Autowired
//    private CustomUserServiceDetails userService;

    private final UserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          UserMapper userMapper, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterModel user) {

        Optional<CustomUser> byUsername = userRepository.findByUsername(user.getUsername());

        if(byUsername.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        if(user.getPassword().length()< 6){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Password must be at least 6 characters long");
        }
        CustomUser mapped = userMapper.map(user);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        mapped.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(mapped);
        return ResponseEntity.status(HttpStatus.CREATED).body("You registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());
        if(authentication.isAuthenticated()){
            return ResponseEntity.ok((new AuthenticationResponse(accessToken, refreshToken)));
        }else{
            throw new BadCredentialsException("bad-credentials");
        }

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String username = jwtUtil.extractUsername(request.getRefreshToken());
        if (username != null) {
            String newAccessToken = jwtUtil.generateAccessToken(username);
            return ResponseEntity.ok(new AuthenticationResponse(newAccessToken, request.getRefreshToken()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Refresh Token");
    }

//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
//        userService.invalidateToken(token);
//        return ResponseEntity.ok("Logged out successfully");
//    }
}
