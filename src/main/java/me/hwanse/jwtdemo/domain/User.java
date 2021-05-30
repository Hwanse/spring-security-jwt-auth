package me.hwanse.jwtdemo.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class User {

  @Id @GeneratedValue(strategy = IDENTITY)
  @Column(name = "users_id")
  private Long id;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false, length = 50)
  private String nickname;

  @Column(nullable = false, unique = true, length = 50)
  private String email;

  @Column(nullable = false, length = 100)
  private String password;

  private LocalDateTime createAt;

  private LocalDateTime modifiedAt;

  private LocalDateTime deletedAt;

  private boolean beDeleted;

  @ManyToMany
  @JoinTable(
    name = "user_authority",
    joinColumns = {@JoinColumn(name = "users_id", referencedColumnName = "users_id")},
    inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "authority_id")}
  )
  private Set<Authority> authorities = new HashSet<>();

  public User(String name, String nickname, String email, String password) {
    this.name = name;
    this.nickname = nickname;
    this.email = email;
    this.password = password;
    this.createAt = LocalDateTime.now();
    this.modifiedAt = LocalDateTime.now();
    this.deletedAt = null;
    this.beDeleted = false;
    this.authorities.add(new Authority("ROLE_USER"));
  }

}
