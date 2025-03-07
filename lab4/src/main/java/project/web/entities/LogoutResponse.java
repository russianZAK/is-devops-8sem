package project.web.entities;

import java.util.Date;
import lombok.Data;

@Data
public class LogoutResponse {

  private int status;
  private String message;
  private Date timestamp;

  public LogoutResponse(int status, String message) {
    this.status = status;
    this.message = message;
    this.timestamp = new Date();
  }
}
