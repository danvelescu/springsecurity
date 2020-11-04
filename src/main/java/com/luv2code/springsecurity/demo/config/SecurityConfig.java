package com.luv2code.springsecurity.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        User.UserBuilder users = User.withDefaultPasswordEncoder();
//
//        auth.inMemoryAuthentication()
//                .withUser(users.username("dan").
//                        password("test").
//                        roles("ADMIN", "EMPLOYEE"));
//        auth.inMemoryAuthentication()
//                .withUser(users.username("Joe").
//                        password("test").
//                        roles( "EMPLOYEE"));
//        auth.inMemoryAuthentication()
//                .withUser(users.username("Radu").
//                        password("test").
//                        roles("MANAGER", "EMPLOYEE"));
//        auth.inMemoryAuthentication()
//                .withUser(users.username("Octavian").
//                        password("test").
//                        roles("EMPLOYEE"));
//        auth.inMemoryAuthentication()
//                .withUser(users.username("Eugen").
//                        password("test").
//                        roles("EMPLOYEE"));
//
//    }

    @Override
    protected void configure(HttpSecurity https) throws Exception {
        https.authorizeRequests()
                .antMatchers("/").hasAnyRole("EMPLOYEE")
                .antMatchers("/leaders/**").hasAnyRole("MANAGER")
                .antMatchers("/systems/**").hasAnyRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/authenticateTheUser")
                .permitAll()
                .and().logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied");
        https.logout();
    }

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username,password,enabled from users where username = ?")
                .authoritiesByUsernameQuery("select username,authority from authorities where username = ?");
    }

}

