package bigtask01.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No data found")
public class NotFoundException extends RuntimeException {

  public NotFoundException(String msg) {
    super(msg);
  }

  public NotFoundException(int id) {
    super("id=" + id);
  }
}
