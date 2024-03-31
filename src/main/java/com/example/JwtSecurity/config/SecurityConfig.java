package com.example.JwtSecurity.config;

import com.example.JwtSecurity.filter.MyFilter;
import com.example.JwtSecurity.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {
    private final UserDetailsServiceImpl myUserDetailsService;
    private final MyFilter myFilter;
    private final AccessDeniedException accessDeniedException;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService, MyFilter myFilter, AccessDeniedException accessDeniedException) {
        this.myUserDetailsService = userDetailsService;
        this.myFilter = myFilter;
        this.accessDeniedException = accessDeniedException;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/login/**", "/register/**", "/hello").permitAll()
                                .requestMatchers("/secure/admin").hasAuthority("ADMIN")
                                .anyRequest().authenticated())
                .userDetailsService(myUserDetailsService)
                .exceptionHandling(e -> e.accessDeniedHandler(accessDeniedException)
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(myFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
