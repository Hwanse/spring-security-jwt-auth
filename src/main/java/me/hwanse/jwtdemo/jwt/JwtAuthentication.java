package me.hwanse.jwtdemo.jwt;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import me.hwanse.jwtdemo.domain.Authority;
import me.hwanse.jwtdemo.domain.User;
import org.springframework.core.style.ToStringCreator;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthentication {

  private final Long id;

  private final String email;

  private final String username;

  private final Set<Authority> authorities;

  public JwtAuthentication(Long id, String email, String username, Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.email = email;
    this.username = username;
    this.authorities = authorities.stream()
      .map(auth -> new Authority(auth.getAuthority()))
      .collect(Collectors.toSet());
  }

  public JwtAuthentication(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.username = user.getName();
    this.authorities = user.getAuthorities();
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getUsername() {
    return username;
  }

  public Set<Authority> getAuthorities() {
    return authorities;
  }

  @Override
  public String toString() {
    return new ToStringCreator(this)
      .append("id", id)
      .append("email", email)
      .append("username", username)
      .append("authorities", authorities)
      .toString();
  }

}
