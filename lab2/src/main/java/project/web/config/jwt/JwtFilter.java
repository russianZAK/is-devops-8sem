package project.web.config.jwt;

import io.jsonwebtoken.Claims;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

/**
 * JwtFilter is a Spring Framework filter responsible for processing JWT (JSON Web Token) authentication
 * within the web application. It extracts the JWT from the request, validates it using the JwtProvider,
 * and sets the authenticated user details in the SecurityContextHolder.
 * This filter is designed to be configured in the Spring Security configuration to secure specific
 * endpoints and enforce JWT-based authentication.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

  private static final String AUTHORIZATION = "Authorization";

  private final JwtProvider jwtProvider;

  /**
   * Performs the JWT authentication process. It extracts the token from the request, validates it using
   * the JwtProvider, and sets the authenticated user details in the SecurityContextHolder.
   *
   * @param request The servlet request
   * @param response The servlet response
   * @param filterChain The filter chain for the next filters in the chain
   * @throws IOException if an I/O error occurs during the execution of this filter
   * @throws ServletException if any servlet-related errors occur during the execution of this filter
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    final String token = getTokenFromRequest((HttpServletRequest) request);
    if (token != null && jwtProvider.validateAccessToken(token)) {
      final Claims claims = jwtProvider.getAccessClaims(token);
      final UsernamePasswordAuthenticationToken jwtInfoToken = JwtUtils.generate(claims);
      SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
    }
    filterChain.doFilter(request, response);
  }

  /**
   * Extracts the JWT token from the Authorization header of the HttpServletRequest.
   *
   * @param request The HttpServletRequest from which to extract the token
   * @return The JWT token or null if it is not present or not in the expected format
   */
  private String getTokenFromRequest(HttpServletRequest request) {
    final String bearer = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }
    return null;
  }
}
