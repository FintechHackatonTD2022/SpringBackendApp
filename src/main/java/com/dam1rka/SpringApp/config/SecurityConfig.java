package com.dam1rka.SpringApp.config;

import com.dam1rka.SpringApp.security.JwtUserDetailsService;
import com.dam1rka.SpringApp.security.jwt.JwtConfigurer;
import com.dam1rka.SpringApp.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService UserDetailsService;

    private AuthenticationManager authenticationManager;

    private static final String ADMIN_ENDPOINT = "/api/admin/**";
    private static final String LOGIN_ENDPOINT = "/api/auth/**";

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, UserDetailsService UserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.UserDetailsService = UserDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(UserDetailsService);
        authenticationManager = authenticationManagerBuilder.build();

        http
                .httpBasic().disable()
                .csrf().disable()

                .authorizeRequests()
                .antMatchers().permitAll()
                .antMatchers(ADMIN_ENDPOINT).hasRole("ADMIN")
                // TODO: Don't forget to uncomment on prod
                //.anyRequest().authenticated()
                .and()
                .authenticationManager(authenticationManager)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));

        return http.build();
    }
}
