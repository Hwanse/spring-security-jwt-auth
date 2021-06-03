package me.hwanse.jwtdemo.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.style.ToStringCreator;

@Entity
@Table(name = "authority")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {

  @Id @GeneratedValue(strategy = IDENTITY)
  @Column(name = "authority_id")
  private Long id;

  @Column(name = "authority_name", nullable = false)
  private String name;

  public Authority(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return new ToStringCreator(this)
      .append("id", id)
      .append("name", name)
      .toString();
  }
}
