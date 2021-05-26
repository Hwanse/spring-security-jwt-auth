package me.hwanse.jwtdemo.jwt;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserContext extends User {

  private final me.hwanse.jwtdemo.domain.User user;

  public UserContext(me.hwanse.jwtdemo.domain.User user, Collection<? extends GrantedAuthority> authorities) {
    super(user.getEmail(), user.getPassword(), authorities);
    this.user = user;
  }

  public me.hwanse.jwtdemo.domain.User getUser() {
    return user;
  }

}
