package com.horizonimmo.config;

import com.horizonimmo.repository.AdminUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(AdminUserRepository adminUserRepository) {
        return email -> adminUserRepository.findByEmail(email)
                .map(admin -> (UserDetails) User.withUsername(admin.getEmail())
                        .password(admin.getPasswordHash())
                        .roles("AGENT")
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Compte agent inconnu : " + email));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // L'API est appelee par l'agent vocal x.ai (server-to-server), protegee
                // a part par XaiApiKeyFilter plutot que par une session de connexion.
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/annonces", "/css/**", "/js/**", "/img/**", "/api/**").permitAll()
                        .requestMatchers("/backoffice/login").permitAll()
                        .requestMatchers("/backoffice/**").authenticated()
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/backoffice/login")
                        .loginProcessingUrl("/backoffice/login")
                        .defaultSuccessUrl("/backoffice", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/backoffice/logout")
                        .logoutSuccessUrl("/")
                        .permitAll());

        return http.build();
    }
}
