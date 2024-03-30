package com.example.config;

import com.example.service.MyUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MyUserDetails myUserDetails;

    @Bean
    public SecurityFilterChain myFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> {

                    cors.configurationSource(new CorsConfigurationSource() {

                        @Override
                        public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                            CorsConfiguration cfg = new CorsConfiguration();

                            cfg.setAllowedOriginPatterns(Collections.singletonList("*"));
                            cfg.setAllowedMethods(Collections.singletonList("*"));
                            cfg.setAllowCredentials(true);
                            cfg.setAllowedHeaders(Collections.singletonList("*"));
                            cfg.setExposedHeaders(Arrays.asList("Authorization"));
                            return cfg;
                        }
                    });

                })

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/students").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/students/{studentId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/students").permitAll()

                        .requestMatchers("/swagger-ui*/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class)

                .formLogin(Customizer.withDefaults()) // for browser
                .httpBasic(Customizer.withDefaults()); // for http client


        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(myUserDetails);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

}
