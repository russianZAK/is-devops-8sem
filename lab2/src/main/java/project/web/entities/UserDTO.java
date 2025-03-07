package project.web.entities;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
  @Email
  private String email;

  @NotNull
  @NotEmpty
  private String username;

  @NotEmpty
  @NotNull
  private String password;

  @NotEmpty
  @NotNull
  private String phoneNumber;

  @NotNull
  private LocalDate birthDate;

  @NotNull
  private Gender gender;

  @NotEmpty
  @NotNull
  private String country;

  @NotEmpty
  @NotNull
  private String region;

  private String city;

  private String town;
}
