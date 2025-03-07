package project.web.entities;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenRequest {
  @NonNull
  @NotEmpty
  public String accessToken;
}
