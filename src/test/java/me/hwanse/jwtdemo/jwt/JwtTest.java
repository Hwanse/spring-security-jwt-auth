package me.hwanse.jwtdemo.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.Claims;
import java.util.Optional;
import me.hwanse.jwtdemo.domain.Authority;
import me.hwanse.jwtdemo.domain.User;
import org.apache.tomcat.util.http.parser.Authorization;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TestInstance(Lifecycle.PER_CLASS)
class JwtTest {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final String AUTHORIZATION = "Authorization";
  private final String APP_SERVER_NAME = "jwt-auth-server";
  private final String SECRET_KEY = "aHdhbnNlLXNwcmluZy1zZWN1cml0eS1qd3QtYXV0aC1zZXJ2ZXItc2VjcmV0LWtleQo";

  private Jwt jwt;
  private User user;

  @BeforeAll
  public void setup() {
    JwtProperty jwtProperty = new JwtProperty();
    jwtProperty.setHeader(AUTHORIZATION);
    jwtProperty.setIssuer(APP_SERVER_NAME);
    jwtProperty.setSecret(SECRET_KEY);
    jwtProperty.setExpiration(3600);

    jwt = new Jwt(jwtProperty);
    user = new User("test", "test", "test@gmail.com", "test");
  }

  @Test
  @DisplayName("토큰 생성 테스트")
  public void create_token() throws Exception {
    // when
    String token = jwt.createToken(user);
    log.info("token : {}", token);

    Claims claims = jwt.verifyToken(token).get();
    log.info("decoded Jwt-Claims : {}", claims);
    Authority role = new Authority(claims.get("roles", String.class));

    // then
    assertThat(token).isNotNull();
    assertThat(claims).isNotNull();
    assertThat(claims.get("email", String.class)).isEqualTo(user.getEmail());
    assertThat(claims.get("username", String.class)).isEqualTo(user.getName());
    assertThat(claims.get("roles", String.class)).isEqualTo(role.getName());
  }

}