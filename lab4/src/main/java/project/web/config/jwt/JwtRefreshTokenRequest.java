package project.web.config.jwt;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRefreshTokenRequest {
  @NonNull
  @NotEmpty
  public String refreshToken;
}