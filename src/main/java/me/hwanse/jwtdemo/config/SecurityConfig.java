package me.hwanse.jwtdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .headers()
      .frameOptions()
      .sameOrigin()
    .and()
      .formLogin().disable()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    .and()
      .authorizeRequests()
      .antMatchers("/api/login").permitAll()
      .antMatchers("/api/join").permitAll()
      .anyRequest().authenticated();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web
      .ignoring()
      .antMatchers("/h2-console/**");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
