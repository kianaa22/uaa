package com.project.security.jwt.config;

import com.project.security.jwt.JWTAuthenticationFilter;
import com.project.security.jwt.data.repository.UserRepository;
import com.project.security.jwt.service.CustomUserServiceDetails;
import com.project.security.jwt.util.JWTUtil;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private CustomUserServiceDetails userService;

    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private JWTAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception {
        DefaultSecurityFilterChain securityFilterChain = http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/login", "/api/auth/logout", "/api/auth/register")
//                        .permitAll() // Allow login/logout
                        .requestMatchers(
                                "api/auth/**",
                                "/public/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JWTAuthenticationFilter(jwtUtil, userService), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .build();
        logFilters(securityFilterChain.getFilters());

        return securityFilterChain;
    }

    private void logFilters(List<Filter> filters) {
        System.out.println("=== Spring Security Filter Chain Order ===");
        for (Filter filter : filters) {
            System.out.println(filter.getClass().getSimpleName());
        }
        System.out.println("=========================================");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
//        return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder(); // Use BCrypt for password encoding
    }

    @Bean
    public AuthenticationManager authManager(CustomUserServiceDetails userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserServiceDetails(userRepository);
    }

        /*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/public/**").permitAll()
                        .anyRequest().authenticated())
                        .formLogin(login->login
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        )
                .formLogin(Customizer.withDefaults())
                .logout(logout -> logout
                                .logoutUrl("/api/logout")
                                .logoutSuccessUrl("/login?logout=true") // Optionally show logout success message
                                .invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .deleteCookies("JSESSIONID")
                        );

        // Use JWT filter
//        return http.addFilter(new JWTAuthenticationFilter(authenticationManager()));
        return http.build();
    }

     */

    /*
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception {
    //        http.authorizeHttpRequests(auth -> auth
    //                        .requestMatchers("/public/**").permitAll()
    //                        .requestMatchers("/admin").hasRole("ADMIN")
    //                        .requestMatchers("/user").hasRole("USER")
    //                        .anyRequest().authenticated())
    //                .formLogin(login->login
    //                        .loginPage("/login")
    //                        .permitAll()
    //                        .defaultSuccessUrl("/home", true)
    //                        .failureUrl("/login?error=true")
    //                        )
    //                .formLogin(Customizer.withDefaults())
    //                .logout(logout -> logout
    //                                .logoutUrl("/api/logout")
    //                                .logoutSuccessUrl("/login?logout=true") // Optionally show logout success message
    //                                .invalidateHttpSession(true)
    //                                .clearAuthentication(true)
    //                                .deleteCookies("JSESSIONID")
    //                        );
    //                .httpBasic();
    //                .logout(Customizer.withDefaults());
    //        return http.build();
        }

     */

    //    @Bean
//    public UserDetailsService userDetails(){
//        UserDetails users = User.withUsername("user")
//                .password(passwordEncoder().encode("user"))
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.withUsername("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("USER", "ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(users, admin);

//    }
//    @Bean
//    public UserDetailsService userDetailsService(){
//        return userServiceDetails;

//    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
}
