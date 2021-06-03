package me.hwanse.jwtdemo.jwt;

import static me.hwanse.jwtdemo.controller.ApiResult.ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.hwanse.jwtdemo.controller.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ApiResult<?> errorResponse = ERROR(HttpStatus.UNAUTHORIZED, "Authentication error (cause: unauthorized)");

  private final ObjectMapper objectMapper;

  public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void commence(HttpServletRequest httpServletRequest,
    HttpServletResponse httpServletResponse, AuthenticationException e)
    throws IOException, ServletException {

    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
    httpServletResponse.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    httpServletResponse.getWriter().flush();
    httpServletResponse.getWriter().close();
  }
}
