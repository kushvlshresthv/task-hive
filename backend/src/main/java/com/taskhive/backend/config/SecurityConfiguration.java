package com.taskhive.backend.config;

import com.taskhive.backend.user_details_service.DatabaseUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((config) -> {
            config.requestMatchers("/register", "/checkUsernameAvailability").permitAll();
            config.requestMatchers("/login", "/test").authenticated();
        });
        http.httpBasic(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService getUserDetailsService() {
        return new DatabaseUserDetailsService();
    }
}
