package me.hwanse.jwtdemo.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
public class User {

  @Id @GeneratedValue(strategy = IDENTITY)
  @Column(name = "users_id")
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false, unique = true)
  private String email;

  private int loginCount;

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
  private Set<Authority> authorities;

}
