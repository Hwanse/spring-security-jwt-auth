package me.hwanse.jwtdemo.controller.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginDto {

  @Email
  private String email;

  @NotNull
  @Size(min = 4, max = 50)
  private String password;

  public LoginDto(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

}
