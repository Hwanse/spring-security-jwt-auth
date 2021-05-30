package me.hwanse.jwtdemo.controller.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import me.hwanse.jwtdemo.domain.Authority;
import me.hwanse.jwtdemo.domain.User;

public class UserDto {

  private Long id;
  private String name;
  private String nickname;
  private String email;
  private LocalDateTime createAt;
  private LocalDateTime modifiedAt;
  private LocalDateTime deletedAt;
  private boolean beDeleted;
  private Set<Authority> authorities = new HashSet<>();

  public UserDto(User user) {
    this.id = user.getId();
    this.name = name;
    this.nickname = nickname;
    this.email = email;
    this.createAt = createAt;
    this.modifiedAt = modifiedAt;
    this.deletedAt = deletedAt;
    this.beDeleted = beDeleted;
    this.authorities = authorities;
  }
}
