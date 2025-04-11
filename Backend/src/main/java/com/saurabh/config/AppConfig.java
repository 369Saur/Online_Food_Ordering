package com.saurabh.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration // Spring ko bata raha hai ki yeh setup ki class hai
@EnableWebSecurity  // Spring Security ko activate kar raha hai taaki authentication aur authorization handle ho sake
public class AppConfig {

    @Bean // Is method ka object Spring khud banayega aur manage karega
    // Bean ek object hota hai jisko Spring automatically create, manage aur inject karta hai.
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.sessionManagement(management->management
                        // Server session maintain nahi karega, har request independent hogi (JWT use hoga)
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                        .authorizeHttpRequests(Authorize->Authorize
                                // "/api/admin/**" wale routes sirf ADMIN aur RESTAURANT_OWNER access kar sakte hain
                                .requestMatchers("/api/admin/**").hasAnyRole("RESTAURANT_OWNER","ADMIN")

                                // "/api/**" wale routes sirf logged-in (authenticated) users access kar sakte hain
                                .requestMatchers("/api/**").authenticated()

                                // Baaki sabhi routes sabko accessible hain (public routes)
                                .anyRequest().permitAll()
                        )
                        // JWT Token validation ka custom filter add kar rahe hain
                        .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)

                        // CSRF security disable kar rahe hain kyunki API me zarurat nahi hoti
                        .csrf(csrf->csrf.disable())

                        // CORS enable kar rahe hain taaki frontend backend se securely connect ho sake
                        .cors(cors->cors.configurationSource(corsConfigurationsource())) ;
        return http.build() ;
    }

    private CorsConfigurationSource corsConfigurationsource(){

        return new CorsConfigurationSource() {

            @Override // indicate method overriding
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                CorsConfiguration cfg = new CorsConfiguration() ;

                // Allowed Origins: Sirf mention URLs se request allow hai
                cfg.setAllowedOrigins(Arrays.asList(
                        "http://localhost:8080/"
                ));

                // Allowed Methods: Sare HTTP methods (GET, POST, PUT, DELETE, etc.) allow hain
                cfg.setAllowedMethods(Collections.singletonList("*")) ;

                // Credentials Allow: Agar `true` hai, to cookies ya authentication headers bhejne ki permission milegi
                cfg.setAllowCredentials(true) ;

                // Allowed Headers: Sare headers allow hain (e.g., Content-Type, Authorization, etc.)
                cfg.setAllowedHeaders(Collections.singletonList("*")) ;

                // Exposed Headers: Kaunse headers browser ko visible honge (e.g., "Authorization")
                cfg.setExposedHeaders(Arrays.asList("Authorization")) ;

                // CORS Policy Cache Time: 3600 sec (1 hour) tak browser CORS rules yaad rakhega
                cfg.setMaxAge(3600L);

                return cfg ;

            }

        } ;

    }

    @Bean
    PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder() ;

    }

}
