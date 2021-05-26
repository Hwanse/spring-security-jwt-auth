package me.hwanse.jwtdemo.repository;

import me.hwanse.jwtdemo.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
