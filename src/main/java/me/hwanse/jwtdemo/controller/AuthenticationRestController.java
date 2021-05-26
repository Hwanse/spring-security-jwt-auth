package me.hwanse.jwtdemo.controller;

import javax.validation.Valid;
import me.hwanse.jwtdemo.controller.dto.LoginDto;
import me.hwanse.jwtdemo.controller.dto.TokenDto;
import me.hwanse.jwtdemo.jwt.JwtAuthenticationToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationRestController {

  private final AuthenticationManager authenticationManager;

  public AuthenticationRestController(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @PostMapping("/api/login")
  public ResponseEntity login(@Valid @RequestBody LoginDto loginDto) {

    Authentication authenticationToken = new JwtAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
    Authentication authenticated = authenticationManager.authenticate(authenticationToken);

    SecurityContextHolder.getContext().setAuthentication(authenticated);
    String jwt = (String) authenticated.getDetails();

    return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + jwt)
                    .body(new TokenDto(jwt));
  }

}
