package project.web.config.jwt;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
  private final String type = "Bearer";

  @NotNull
  @NotEmpty
  private String accessToken;

  @NotNull
  @NotEmpty
  private String refreshToken;
}