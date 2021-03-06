package me.hwanse.jwtdemo.controller.dto;

import lombok.Getter;
import me.hwanse.jwtdemo.domain.User;
import org.springframework.core.style.ToStringCreator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class JoinRequest {

  @Email
  private final String email;

  @NotBlank
  @Size(max = 50)
  private final String username;

  @NotBlank
  @Size(max = 50)
  private final String nickname;

  @NotBlank
  @Size(max = 100)
  private final String password;

  public JoinRequest(String email, String username, String nickname, String password) {
    this.email = email;
    this.username = username;
    this.nickname = nickname;
    this.password = password;
  }

  public User newUser() {
    return new User(username, nickname, email, password);
  }

  @Override
  public String toString() {
    return new ToStringCreator(this)
      .append("email", email)
      .append("username", username)
      .append("nickname", nickname)
      .toString();
  }
}
