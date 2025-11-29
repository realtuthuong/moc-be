package com.example.MocBE.config;


import com.example.MocBE.security.CustomUserDetailsService;
import com.example.MocBE.security.Endpoints;
import com.example.MocBE.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final CustomUserDetailsService userDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/ws/**").permitAll()

                        // Public
                        .requestMatchers(HttpMethod.GET, Endpoints.PUBLIC_GET_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, Endpoints.PUBLIC_POST_ENDPOINTS).permitAll()

                        // DATA MANAGER
                        .requestMatchers(HttpMethod.GET, Endpoints.MANAGER_GET_ENDPOINTS).hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, Endpoints.MANAGER_PUT_ENDPOINTS).hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, Endpoints.MANAGER_POST_ENDPOINTS).hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, Endpoints.MANAGER_DELETE_ENDPOINTS).hasAnyRole("MANAGER", "ADMIN")

                        // CHEF
                        .requestMatchers(HttpMethod.GET, Endpoints.CHEF_GET_ENDPOINTS).hasAnyRole("CHEF", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, Endpoints.CHEF_PUT_ENDPOINTS).hasAnyRole("CHEF", "ADMIN")


                        // STAFF
                        .requestMatchers(HttpMethod.GET, Endpoints.STAFF_GET_ENDPOINTS).hasAnyRole("STAFF", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, Endpoints.STAFF_PUT_ENDPOINTS).hasAnyRole("STAFF", "ADMIN")
                        .requestMatchers(HttpMethod.POST, Endpoints.STAFF_POST_ENDPOINTS).hasAnyRole("STAFF", "ADMIN")

                        // DATA ENTRY
                        .requestMatchers(HttpMethod.GET, Endpoints.DATA_ENTRY_GET_ENDPOINTS).hasAnyRole("DATA_ENTRY", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, Endpoints.DATA_ENTRY_PUT_ENDPOINTS).hasAnyRole("DATA_ENTRY", "ADMIN")
                        .requestMatchers(HttpMethod.POST, Endpoints.DATA_ENTRY_POST_ENDPOINTS).hasAnyRole("DATA_ENTRY", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, Endpoints.DATA_ENTRY_DELETE_ENDPOINTS).hasAnyRole("DATA_ENTRY", "ADMIN")

                        // ADMIN có quyền trên tất cả các request
                        .anyRequest().hasRole("ADMIN")
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


