package me.hwanse.jwtdemo.jwt;

import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER = "Bearer";

  private final Jwt jwt;

  public JwtAuthenticationFilter(Jwt jwt) {
    this.jwt = jwt;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
    FilterChain filterChain) throws ServletException, IOException {

    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    String requestURI = httpServletRequest.getRequestURI();

    if (isUnauthenticatedUser()) {
      String token = resolveToken(httpServletRequest);
      if (token != null) {
        Claims claims = jwt.verifyToken(token).orElseThrow();

        String username = claims.get("username", String.class);
        String email = claims.get("email", String.class);
        String roles = claims.get("roles", String.class);
        Collection<? extends GrantedAuthority> authorities =
          Arrays.stream(roles.split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());

        if (validateClaims(username, email, roles)) {
          JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(
            new User(email, null, authorities), null, authorities);
          jwtAuthenticationToken.setDetails(token);
          SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
          log.debug("인증 정보를 저장했습니다. '{}' / URI : {}", jwtAuthenticationToken.getPrincipal(),
            requestURI);
        }
      }
    }

    filterChain.doFilter(request, response);

  }

  /**
   * 요청 URL이 '/api/login', '/api/join'일 경우 필터를 거치지 않는다.
   * true 리턴 시 다음 필터로 skip, false 리턴 시 이 Filter 내부에 정의한 doFilterInternal 를 거친다.
   */
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    AntPathMatcher pathMatcher = new AntPathMatcher();
    String requestURI = request.getRequestURI();

    if (pathMatcher.match("/api/login", requestURI) || pathMatcher.match("/api/join", requestURI)) {
      return true;
    }

    return false;
  }

  private boolean validateClaims(String username, String email, String roles) {
    if (StringUtils.hasText(username) && StringUtils.hasText(email) && StringUtils.hasText(roles)) {
      return true;
    }
    return false;
  }

  private boolean isUnauthenticatedUser() {
    return SecurityContextHolder.getContext().getAuthentication() == null;
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
      return bearerToken.split(" ")[1];
    }
    return null;
  }

}
