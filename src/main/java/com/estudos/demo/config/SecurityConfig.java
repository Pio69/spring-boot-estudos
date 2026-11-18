package com.estudos.demo.config;

import com.estudos.demo.dto.Produto;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .authorizeHttpRequests(autorizacao -> autorizacao
                .requestMatchers(HttpMethod.GET, "/produto", "/produto/**")
                    .permitAll()
                .requestMatchers(HttpMethod.GET, "/categoria", "/categoria/**")
                    .permitAll()
                .requestMatchers(HttpMethod.POST, "/produto")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/categoria")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/produto/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/categoria/**")
                    .hasRole("ADMIN")
                .anyRequest()
                    .authenticated()
            )
            .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder encoder,
            @Value("${APP_CONSULTA_PASSWORD}") String senhaConsulta,
            @Value("${APP_ADMIN_PASSWORD}") String senhaAdmin) {

        UserDetails consulta = User.builder()
                .username("consulta")
                .password(encoder.encode(senhaConsulta))
                .roles("CONSULTA")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode(senhaAdmin))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(consulta, admin);
    }
}