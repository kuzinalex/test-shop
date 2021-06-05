package com.kuzin.testTask.config;

import com.kuzin.testTask.repositories.UserRepository;
import com.kuzin.testTask.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/items").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.POST,"/items").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/items/{id}").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.PATCH,"/items/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/items/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/items/{id}").hasRole("USER")
                .antMatchers(HttpMethod.PATCH,"/items/{id}/forceUpdate").hasRole("ADMIN")
                .antMatchers("/cart/**").hasRole("USER")
                .and()
                .httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(new UserService(userRepository));
        return authenticationProvider;
    }
}
