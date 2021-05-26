package me.hwanse.jwtdemo.repository;

import me.hwanse.jwtdemo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

}
