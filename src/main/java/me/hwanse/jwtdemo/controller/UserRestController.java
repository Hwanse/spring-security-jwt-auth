package me.hwanse.jwtdemo.controller;

import me.hwanse.jwtdemo.controller.dto.JoinRequest;
import me.hwanse.jwtdemo.controller.dto.UserDto;
import me.hwanse.jwtdemo.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

  private final UserService userService;

  public UserRestController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/api/join")
  public ApiResult<?> join(@RequestBody JoinRequest joinRequest) {
    return ApiResult.OK(
      new UserDto(userService.join(joinRequest.newUser()))
    );
  }

}
