package org.shiloh.Shiloh;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // ❗ enable later with CSRF tokens
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/books/**").authenticated()
                .requestMatchers("/auth/**").permitAll()
            )
            .formLogin(form -> form
                .loginProcessingUrl("/auth/login")   // POST here with username+password
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1) // one session per user
            );
        return http.build();
    }
}
