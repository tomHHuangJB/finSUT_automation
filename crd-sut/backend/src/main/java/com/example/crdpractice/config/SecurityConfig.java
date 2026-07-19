package com.example.crdpractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health/**", "/api/v1/health").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails pmUser = User.withUsername("pm_user")
                .password(passwordEncoder.encode("password"))
                .roles("PORTFOLIO_MANAGER")
                .build();
        UserDetails complianceUser = User.withUsername("compliance_user")
                .password(passwordEncoder.encode("password"))
                .roles("COMPLIANCE_OFFICER")
                .build();
        UserDetails traderUser = User.withUsername("trader_user")
                .password(passwordEncoder.encode("password"))
                .roles("TRADER")
                .build();
        UserDetails operationsUser = User.withUsername("operations_user")
                .password(passwordEncoder.encode("password"))
                .roles("OPERATIONS")
                .build();
        UserDetails adminUser = User.withUsername("admin_user")
                .password(passwordEncoder.encode("password"))
                .roles("ADMIN")
                .build();
        UserDetails readOnlyUser = User.withUsername("readonly_user")
                .password(passwordEncoder.encode("password"))
                .roles("READ_ONLY")
                .build();
        return new InMemoryUserDetailsManager(
                pmUser,
                complianceUser,
                traderUser,
                operationsUser,
                adminUser,
                readOnlyUser);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
