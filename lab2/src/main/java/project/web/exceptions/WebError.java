package project.web.exceptions;

import java.util.Date;
import lombok.Data;

@Data
public class WebError {
  private int status;
  private String message;
  private Date timestamp;

  public WebError(int status, String message) {
    this.status = status;
    this.message = message;
    this.timestamp = new Date();
  }
}