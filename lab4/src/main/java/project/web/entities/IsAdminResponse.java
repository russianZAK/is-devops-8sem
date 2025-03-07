package project.web.entities;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IsAdminResponse {
  @NotNull
  @NotEmpty
  private boolean isAdmin;
}
