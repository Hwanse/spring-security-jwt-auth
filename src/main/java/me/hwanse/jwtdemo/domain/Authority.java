package me.hwanse.jwtdemo.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "authority")
@Getter
public class Authority {

  @Id @GeneratedValue(strategy = IDENTITY)
  @Column(name = "authority_id")
  private Long id;

  @Column(name = "authority_name", nullable = false)
  private String name;

}
