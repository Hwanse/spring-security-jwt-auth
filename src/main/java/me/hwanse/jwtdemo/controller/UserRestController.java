package me.hwanse.jwtdemo.controller;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.jwtdemo.controller.dto.JoinRequest;
import me.hwanse.jwtdemo.controller.dto.UserDto;
import me.hwanse.jwtdemo.jwt.JwtAuthentication;
import me.hwanse.jwtdemo.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserRestController {

  private final UserService userService;

  public UserRestController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/api/join")
  public ApiResult<?> join(@Valid @RequestBody JoinRequest joinRequest) {
    return ApiResult.OK(
      new UserDto(userService.join(joinRequest.newUser()))
    );
  }

  @GetMapping("/api/me")
  public ApiResult<?> me(@AuthenticationPrincipal JwtAuthentication authentication) {
    return ApiResult.OK(
      new UserDto(userService.getUser(authentication.getId()))
    );
  }
}
