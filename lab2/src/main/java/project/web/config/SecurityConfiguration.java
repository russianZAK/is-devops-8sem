package project.web.config;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import project.web.config.jwt.JwtFilter;

/**
 * Configuration class for Spring Security.
 * This class extends {@link org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter}
 * to provide custom configurations for securing the application using Spring Security.</p>
 *
 * The class configures various aspects of security, including authentication, authorization, CORS, and JWT token filtering.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final JwtFilter jwtFilter;

  /**
   * Configures the password encoder for authentication.
   * @return The configured password encoder.
   */
  @Bean(name = "pwdEncoder")
  public PasswordEncoder getPasswordEncoder() {
    DelegatingPasswordEncoder delPasswordEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories
        .createDelegatingPasswordEncoder();
    BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
    delPasswordEncoder.setDefaultPasswordEncoderForMatches(bcryptPasswordEncoder);
    return delPasswordEncoder;
  }

  /**
   * Configures the CORS (Cross-Origin Resource Sharing) settings for the application.
   *
   * @return The configured CORS configuration source.
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  /**
   * Configures HTTP security settings for the application.
   * @param http The HttpSecurity object to be configured.
   * @throws Exception If an error occurs while configuring HTTP security.
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and() // add this line to enable cors
        .httpBasic().and().csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
        .authorizeRequests()
        .antMatchers("/auth/register", "/auth/login", "/auth/get-new-access-token", "/user/check-username-exists/**", "/user/check-email-exists/**").permitAll()
        .antMatchers("/user/**").hasRole("USER")
        .antMatchers("/user-role/**").hasRole("ADMIN")
        .anyRequest().authenticated()
        .and().formLogin();

    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
