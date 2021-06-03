package me.hwanse.jwtdemo.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.jwtdemo.domain.Authority;
import me.hwanse.jwtdemo.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class Jwt {

  private final JwtProperty jwtProperty;
  private final Key key;
  private final String HEADER_TYPE = "JWT";

  public Jwt(JwtProperty jwtProperty) {
    this.jwtProperty = jwtProperty;
    this.key = settingKey(jwtProperty);
  }

  public Key settingKey(JwtProperty jwtProperty) {
    byte[] decodedKeyBytes = Decoders.BASE64.decode(jwtProperty.getSecret());
    SecretKey secretKey = Keys.hmacShaKeyFor(decodedKeyBytes);
    return secretKey;
  }

  public String createToken(User user) {
    Date exp = new Date();
    exp.setTime(exp.getTime() + (jwtProperty.getExpiration() * 1000L));

    return Jwts.builder()
              .setHeader(defaultHeader())
              .setIssuer(jwtProperty.getIssuer())
              .setSubject("userInfo")
              .setClaims(setClaims(user))
              .setExpiration(exp)
              .signWith(key, SignatureAlgorithm.HS256)
              .compact();
  }

  public Optional<Claims> verifyToken(String token) {
    Claims claims = null;
    try {
      claims = Jwts.parserBuilder()
                  .setSigningKey(key)
                  .build()
                  .parseClaimsJws(token)
                  .getBody();

    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.debug("잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {
      log.debug("만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException e) {
      log.debug("지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      log.debug("JWT 토큰이 잘못되었습니다.");
    }

    return Optional.ofNullable(claims);
  }

  public Optional<Authentication> getAuthentication(Claims claims, String token) {
    String seq = claims.get("seq", String.class);
    String username = claims.get("username", String.class);
    String email = claims.get("email", String.class);
    String roles = claims.get("roles", String.class);
    Collection<? extends GrantedAuthority> authorities =
      Arrays.stream(roles.split(","))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());

    if (validateClaims(seq, username, email, roles)) {
      JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(
        new JwtAuthentication(Long.valueOf(seq), email, username, authorities), null, authorities);
      jwtAuthenticationToken.setDetails(token);
      return Optional.ofNullable(jwtAuthenticationToken);
    } else {
      return Optional.empty();
    }
  }

  private boolean validateClaims(String seq, String username, String email, String roles) {
    if (StringUtils.hasText(seq) && StringUtils.hasText(username)
      && StringUtils.hasText(email) && StringUtils.hasText(roles)) {
      return true;
    }

    return false;
  }

  public Map<String, Object> defaultHeader() {
    Map<String, Object> headers = new HashMap<>();
    headers.put("typ", HEADER_TYPE);
    headers.put("alg", SignatureAlgorithm.HS256.name());
    return headers;
  }

  public Map<String, Object> setClaims(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("seq", String.valueOf(user.getId()));
    claims.put("username", user.getName());
    claims.put("email", user.getEmail());
    claims.put("roles", authoritiesString(user.getAuthorities()));
    return claims;
  }

  private String authoritiesString(Set<Authority> authorities) {
    return authorities.stream()
                    .map(Authority::getName)
                    .collect(Collectors.joining(","));
  }

}
