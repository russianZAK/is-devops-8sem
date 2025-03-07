package project.web.config.jwt;

import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import project.web.entities.Role;

/**
 * JwtUtils is a utility class for handling JWT (JSON Web Token) related operations,
 * such as generating Spring Security authentication tokens based on JWT claims.
 * his class provides a set of static methods for JWT-related functionalities.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

  /**
   * Generates a Spring Security authentication token based on the provided JWT claims.
   * @param claims The JWT claims from which to generate the authentication token.
   * @return The generated authentication token.
   */
  public static UsernamePasswordAuthenticationToken generate(Claims claims) {
    Set<GrantedAuthority> authorities = getRoles(claims).stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .collect(Collectors.toSet());

    return new UsernamePasswordAuthenticationToken(
        claims.getSubject(),
        null,
        authorities);
  }

  /**
   * Retrieves the roles from the JWT claims and converts them into a set of Spring Security GrantedAuthority objects.
   * @param claims The JWT claims from which to extract roles.
   * @return A set of GrantedAuthority objects representing the roles.
   */
  private static Set<Role> getRoles(Claims claims) {
    final List<String> roles = claims.get("roles", List.class);
    return roles.stream()
        .map(Role::valueOf)
        .collect(Collectors.toSet());
  }
}