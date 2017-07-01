package com.basicBudgetBuilder;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jersey.JerseyProperties;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.*;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by Hanzi Jing on 4/04/2017.
 */

@Configuration
@EnableWebSecurity
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
//@Order(ManagementServerProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .httpBasic()
                    .and()
                    .authorizeRequests()
                    .antMatchers("/index.html", "/#/register1", "/#/forgot_password", "/#/login", "/**", "/login", "/resetPassword*").permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .csrf().disable();
//                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        }



    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}