package me.hwanse.jwtdemo.jwt;

import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

        if (canRefresh(claims, 300 * 1000L)) {  // 5분 전
          String refreshedToken = jwt.getRefreshedToken(token);
          response.setHeader(AUTHORIZATION_HEADER, String.format("%s %s", BEARER, refreshedToken));
          log.info("before token : {} / refreshed token : {}", token,refreshedToken);
        }

        Authentication authentication = jwt.getAuthentication(claims, token).orElseThrow();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("인증 정보를 저장했습니다. '{}' / URI : {}", authentication.getPrincipal(),requestURI);
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

    if (pathMatcher.match("/login", requestURI)
      || pathMatcher.match("/api/login", requestURI)
      || pathMatcher.match("/api/join", requestURI)) {
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

  private boolean canRefresh(Claims claims, long refreshRangeMills) {
    Date now = new Date();
    Date expiration = claims.getExpiration();

    if (expiration != null) {
      long remainTimeMills = expiration.getTime() - now.getTime();
      return remainTimeMills < refreshRangeMills;
    }
    return false;
  }

}
