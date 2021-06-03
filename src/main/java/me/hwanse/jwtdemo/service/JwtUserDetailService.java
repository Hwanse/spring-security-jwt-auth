package me.hwanse.jwtdemo.service;

import java.util.Set;
import java.util.stream.Collectors;
import me.hwanse.jwtdemo.domain.User;
import me.hwanse.jwtdemo.jwt.UserContext;
import me.hwanse.jwtdemo.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  public JwtUserDetailService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findWithAuthorityByEmail(email)
                  .map(user -> createUserContext(user))
                  .orElseThrow(() -> new UsernameNotFoundException(email + " 해당 계정을 찾을 수 없습니다."));
  }

  private UserContext createUserContext(User user) {
    Set<SimpleGrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
      .map(authority -> new SimpleGrantedAuthority(authority.getName()))
      .collect(Collectors.toSet());

    return new UserContext(user, grantedAuthorities);
  }
}
