package me.hwanse.jwtdemo.repository;

import java.util.Optional;
import me.hwanse.jwtdemo.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  @EntityGraph(attributePaths = "authorities")
  Optional<User> findWithAuthorityByEmail(String email);

}
