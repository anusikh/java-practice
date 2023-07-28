package com.anusikh.authservice.config;

import com.anusikh.authservice.filter.JwtAuthFilter;
import com.anusikh.authservice.service.UserInfoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

// LOGIC:
// We have 2 security configs, the lower order one (@Order(1)) is initialized first
// and then the other one
// The lowerorder one enables all graphql api's by default, if we want protection for these graphql
// routes, add @PreAuthorize in the controller above the route
// Since we want to protect /auth/welcome, added it to the GraphQlSecurityConfig too

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Order(2)
    @Configuration
    public class JwtSecurityConfig {
        @Autowired
        private JwtAuthFilter jwtAuthFilter;

        @Bean
        public UserDetailsService userDetailsService() {
            return new UserInfoUserDetailsService();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(userDetailsService());
            authenticationProvider.setPasswordEncoder(passwordEncoder());
            return authenticationProvider;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
            return httpSecurity
                    .authorizeHttpRequests(x -> x.requestMatchers("/auth/**"))
                    .csrf(x -> x.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                            .ignoringRequestMatchers("/auth/new", "/auth/authenticate", "/auth/oauth",
                                    "/auth/sam"))
                    .authorizeHttpRequests(x -> x.requestMatchers("/auth/new", "/auth/authenticate", "/auth/oauth",
                            "/auth/sam").permitAll())
                    .authorizeHttpRequests(x -> x.requestMatchers("/auth/welcome").authenticated()) // protected routes
                    .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }
    }

    // this is the config for graphql, we are allowing all requests to pass through
    // filter
    @Order(1)
    @Configuration
    public class GraphqlSecurityConfig {
        @Autowired
        private JwtAuthFilter jwtAuthFilter;

        @Bean
        public SecurityFilterChain securityFilterChain2(HttpSecurity httpSecurity) throws Exception {
            return httpSecurity
                    .cors(x -> x.disable())
                    .csrf(x -> x.disable())
                    .authorizeHttpRequests(x -> x.requestMatchers("/auth/welcome").authenticated())
                    .authorizeHttpRequests(x -> x.anyRequest().permitAll())
                    .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }
    }
}
