package com.anusikh.libraryspringbootproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.anusikh.libraryspringbootproject.security.JWTFilter;
import com.anusikh.libraryspringbootproject.service.PersonDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private PersonDetailsService personDetailsService;
    private JWTFilter filter;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService, JWTFilter filter) {
        this.personDetailsService = personDetailsService;
        this.filter = filter;
    }

    // For rest
    //    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .httpBasic().disable()
//                .cors()
//                .and()
//                .authorizeHttpRequests((authorize) ->
//                        authorize.requestMatchers("/api/register", "/api/login").permitAll()
//                    .anyRequest().authenticated())
//                .userDetailsService(personDetailsService)
//                .exceptionHandling()
//                .accessDeniedHandler(new SimpleAccessDeniedHandler())
//                .authenticationEntryPoint(new SimpleAuthenticationEntryPoint())
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }

    // For Normal
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http    .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/auth/**","/books/**","/people/**","/login").permitAll()
                                .anyRequest().hasAnyRole("ADMIN","USER"))
                .userDetailsService(personDetailsService)
                .formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/people", true)
                .failureUrl("/auth/login?error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/auth/login");
        return http.build();
    }

    @Bean
    protected PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


