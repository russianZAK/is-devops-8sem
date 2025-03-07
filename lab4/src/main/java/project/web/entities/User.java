package project.web.entities;

import java.time.LocalDate;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "users", schema = "public")
@Data
@NoArgsConstructor
public class User {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private Long id;

  @Basic
  @Column(name = "email", nullable = false, length = 255, unique = true)
  @Email
  private String email;

  @Basic
  @Column(name = "username", nullable = false, length = 255, unique = true)
  private String username;

  @Basic
  @Column(name = "password", nullable = false, length = 255)
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<UserRole> roles;

  @Column(name = "accountnonexpired", nullable = false)
  private boolean accountNonExpired;

  @Column(name = "accountnonlocked", nullable = false)
  private boolean accountNonLocked;

  @Column(name = "credentialsnonexpired", nullable = false)
  private boolean credentialsNonExpired;

  @Column(name = "enabled", nullable = false)
  private boolean enabled;

  @Basic
  @Column(name = "phone_number", nullable = false, length = 20)
  private String phoneNumber;

  @Basic
  @Column(name = "birth_date", nullable = false)
  private LocalDate birthDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "gender", nullable = false, length = 10)
  private Gender gender;

  @Basic
  @Column(name = "country", nullable = false, length = 100)
  private String country;

  @Basic
  @Column(name = "region", nullable = false, length = 100)
  private String region;

  @Basic
  @Column(name = "city", nullable = true, length = 100)
  private String city;

  @Basic
  @Column(name = "town", nullable = true, length = 100)
  private String town;

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
    this.password = bcryptPasswordEncoder.encode(password);
  }

}

enum Gender {
  MALE, FEMALE, OTHER
}
