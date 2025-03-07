package project.web.config.jwt;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest {
  @NotNull
  @NotEmpty
  private String usernameOrEmail;

  @NotNull
  @NotEmpty
  private String password;
}
