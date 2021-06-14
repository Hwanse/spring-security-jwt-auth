package me.hwanse.jwtdemo.config;

import me.hwanse.jwtdemo.jwt.Jwt;
import me.hwanse.jwtdemo.jwt.JwtAccessDenied;
import me.hwanse.jwtdemo.jwt.JwtAuthenticationEntryPoint;
import me.hwanse.jwtdemo.jwt.JwtAuthenticationFilter;
import me.hwanse.jwtdemo.jwt.JwtAuthenticationProvider;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtAccessDenied jwtAccessDenied;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final Jwt jwt;

  public SecurityConfig(JwtAccessDenied jwtAccessDenied,
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, Jwt jwt) {
    this.jwtAccessDenied = jwtAccessDenied;
    this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    this.jwt = jwt;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .formLogin().disable()
      .headers()
      .frameOptions()
      .sameOrigin()
    .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    .and()
      .addFilterBefore(new JwtAuthenticationFilter(jwt), UsernamePasswordAuthenticationFilter.class)
      .authorizeRequests()
      .antMatchers("/login").permitAll()
      .antMatchers("/signup").permitAll()
      .antMatchers("/api/login").permitAll()
      .antMatchers("/api/join").permitAll()
      .anyRequest().authenticated()
    .and()
      .exceptionHandling()
      .authenticationEntryPoint(jwtAuthenticationEntryPoint)
      .accessDeniedHandler(jwtAccessDenied);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web
      .ignoring()
      .antMatchers("/h2-console/**", "/node_modules/**")
      .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(jwtAuthenticationProvider());
  }

  @Bean
  public JwtAuthenticationProvider jwtAuthenticationProvider() {
    return new JwtAuthenticationProvider();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
