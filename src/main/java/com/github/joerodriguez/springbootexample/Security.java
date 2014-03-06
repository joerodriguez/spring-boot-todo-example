package com.github.joerodriguez.springbootexample;

import com.github.joerodriguez.springbootexample.registration.UserEntity;
import com.github.joerodriguez.springbootexample.registration.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
        auth.authenticationProvider(new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String email = (String) authentication.getPrincipal();
                String providedPassword = (String) authentication.getCredentials();
                UserEntity user = userService.findAndAuthenticateUser(email, providedPassword);
                if (user == null) {
                    throw new BadCredentialsException("Username/Password does not match for " + authentication.getPrincipal());
                }

                return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return true;
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // TODO remove when thymeleaf is upgraded
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/todo-list")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/logout").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/resources/**").permitAll()
                .anyRequest().authenticated();
    }
}
