package me.hwanse.jwtdemo.service;

import me.hwanse.jwtdemo.domain.User;
import me.hwanse.jwtdemo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User join(User user) {
    if (isEmailDuplicated(user.getEmail())) {
      throw new IllegalArgumentException("Email is Duplicated. Please Input another email address.");
    }

    User joinUser = user.toBuilder()
      .password(passwordEncoder.encode(user.getPassword()))
      .build();

    return userRepository.save(joinUser);
  }

  private boolean isEmailDuplicated(String email) {
    return userRepository.findWithAuthorityByEmail(email).isPresent();
  }

}
