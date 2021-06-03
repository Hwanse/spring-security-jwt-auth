package me.hwanse.jwtdemo.controller.dto;

import lombok.Getter;
import me.hwanse.jwtdemo.domain.Authority;
import me.hwanse.jwtdemo.domain.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
public class UserDto {

  private Long id;
  private String name;
  private String nickname;
  private String email;
  private LocalDateTime createAt;
  private LocalDateTime modifiedAt;
  private LocalDateTime deletedAt;
  private boolean beDeleted;
  private Set<Authority> authorities;

  public UserDto(User user) {
    this.id = user.getId();
    this.name = user.getName();;
    this.nickname = user.getNickname();
    this.email = user.getEmail();
    this.createAt = user.getCreateAt();
    this.modifiedAt = user.getModifiedAt();
    this.deletedAt = user.getDeletedAt();
    this.beDeleted = user.isBeDeleted();
    this.authorities = new HashSet<>(user.getAuthorities());
  }

}
