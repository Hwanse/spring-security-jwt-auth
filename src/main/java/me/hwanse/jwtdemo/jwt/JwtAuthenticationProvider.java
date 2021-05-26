package me.hwanse.jwtdemo.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class JwtAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private Jwt jwt;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String email = (String) authentication.getPrincipal();
    String password = (String) authentication.getCredentials();

    UserContext userContext = (UserContext) userDetailsService.loadUserByUsername(email);

    if (!passwordEncoder.matches(password, userContext.getPassword())) {
      throw new BadCredentialsException("Invalid Password");
    }
    JwtAuthenticationToken jwtAuthenticationToken =
      new JwtAuthenticationToken(userContext.getUser(), null, userContext.getAuthorities());

    String newJwt = jwt.createToken(userContext.getUser());
    jwtAuthenticationToken.setDetails(newJwt);
    return jwtAuthenticationToken;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return JwtAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
