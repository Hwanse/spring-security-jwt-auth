package me.hwanse.jwtdemo.controller;

import static me.hwanse.jwtdemo.controller.ApiResult.OK;

import javax.validation.Valid;
import me.hwanse.jwtdemo.controller.dto.LoginDto;
import me.hwanse.jwtdemo.controller.dto.TokenDto;
import me.hwanse.jwtdemo.jwt.JwtAuthenticationToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationRestController {

  private final AuthenticationManager authenticationManager;
  private final String BEARER = "Bearer";

  public AuthenticationRestController(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @PostMapping("/api/login")
  public ResponseEntity<ApiResult>  login(@Valid @RequestBody LoginDto loginDto) {
    Authentication authenticationToken = new JwtAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
    Authentication authenticated = authenticationManager.authenticate(authenticationToken);

    SecurityContextHolder.getContext().setAuthentication(authenticated);
    String jwt = (String) authenticated.getDetails();

    return ResponseEntity.ok()
                    .header("Authorization",  String.format("%s %s", BEARER, jwt))
                    .body(
                      OK(new TokenDto(String.format("%s %s", BEARER, jwt)))
                    );
  }

}
