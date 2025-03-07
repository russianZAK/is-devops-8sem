package project.web.entities;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsernameOrEmailRequest {
  @NonNull
  @NotEmpty
  public String usernameOrEmail;

}
