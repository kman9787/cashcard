package com.example.cashcard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration 
public class SecurityConfig {

    @Bean 
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       return http
            .authorizeHttpRequests(request -> request
                .requestMatchers("/cashcards/**")
                .hasRole("CARD-OWNER"))
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
        User.UserBuilder users = User.builder();
        UserDetails sarah = users
                .username("sarah1")
                .password(passwordEncoder.encode("abc123"))
                .roles("CARD-OWNER")
                .build();

        UserDetails hankOwnsNoCards = users
                .username("hank-owns-no-cards")
                .password(passwordEncoder.encode("qrs456"))
                .roles("NON-OWNER")
                .build();
        return new InMemoryUserDetailsManager(sarah, hankOwnsNoCards);
    }
}

/*

In this lab you learned how to use Spring Security to ensure that only authenticated, authorized users have access to the Family Cash Card API. In addition, you followed best practices in the Controller and Repository layers of our application to ensure that only the correct users have access to their (and only their) Cash Card data. You also learned that Spring Security not only protects our requests, but it also modifies our API's error handling as to avoid "leaking" information about crashes and other internal operations occurring with our application.

*/
