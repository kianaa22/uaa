package com.project.security.jwt.service;

import com.project.security.jwt.data.entity.CustomUser;
import com.project.security.jwt.data.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserServiceDetails implements UserDetailsService {

//    @Autowired
    private final UserRepository userRepository;

    public CustomUserServiceDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser customUser = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("username "+username+" not found"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(customUser.getUsername())
                .password(customUser.getPassword())
//                .roles(customUser.getRole())
                .build();
    }
}
