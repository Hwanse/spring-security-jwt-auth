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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessDenied implements AccessDeniedHandler {

  private final ApiResult<?> errorResponse = ERROR(HttpStatus.FORBIDDEN, "Authentication error (cause: forbidden)");

  private final ObjectMapper objectMapper;

  public JwtAccessDenied(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
    AccessDeniedException e) throws IOException, ServletException {

    httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
    httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
    httpServletResponse.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    httpServletResponse.getWriter().flush();
    httpServletResponse.getWriter().close();
  }
}
