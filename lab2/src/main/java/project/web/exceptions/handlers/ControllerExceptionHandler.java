package project.web.exceptions.handlers;

import io.jsonwebtoken.JwtException;
import java.util.List;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import project.web.exceptions.UserAlreadyExistsException;
import project.web.exceptions.WebError;

/**
 * Global exception handler for controllers.
 *
 * <p>This class provides centralized exception handling for various exceptions that may occur during API requests,
 * returning appropriate HTTP responses with error details.</p>
 *
 * <p>It uses the {@link org.springframework.web.bind.annotation.ControllerAdvice} annotation to provide centralized
 * exception handling across multiple controllers.</p>
 *
 * <p>Exceptions handled include {@link org.springframework.security.core.userdetails.UsernameNotFoundException},
 * {@link org.springframework.security.authentication.BadCredentialsException}, {@link java.lang.IllegalArgumentException},
 * {@link java.lang.Exception}, {@link org.springframework.http.converter.HttpMessageNotReadableException},
 * {@link org.springframework.web.bind.MethodArgumentNotValidException}, {@link java.lang.IllegalStateException},
 * {@link org.springframework.web.servlet.NoHandlerFoundException}, {@link io.jsonwebtoken.JwtException},
 * and {@link project.web.exceptions.UserAlreadyExistsException}.</p>
 *
 * <p>For each exception, an appropriate {@link org.springframework.http.ResponseEntity} is created with a
 * {@link project.web.exceptions.WebError} containing the HTTP status code and error message.</p>
 *
 * <p>{@code handleUsernameNotFoundException}, {@code handleBadCredentialsException}, {@code handleIllegalArgumentException},
 * {@code handleException}, {@code handleHttpMessageNotReadable}, {@code handleHttpMessageNotReadable},
 * {@code handleIllegalStateException}, {@code handleNoHandlerFoundException}, {@code handleJwtException}, and
 * {@code handleUsernameAlreadyExistsException} are exception handling methods for specific exceptions.</p>
 */
@ControllerAdvice
public class ControllerExceptionHandler {

  /**
   * Handles the {@link org.springframework.security.core.userdetails.UsernameNotFoundException}.
   *
   * @param e The exception to handle.
   * @return ResponseEntity with the appropriate error details.
   */
  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
    return new ResponseEntity<>(new WebError(HttpStatus.BAD_REQUEST.value(),  e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles the {@link org.springframework.security.authentication.BadCredentialsException}.
   *
   * @param e The exception to handle.
   * @return ResponseEntity with the appropriate error details.
   */
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e) {
    return new ResponseEntity<>(new WebError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()),
        HttpStatus.UNAUTHORIZED);
  }

  /**
   * Handles the {@link java.lang.IllegalArgumentException}.
   *
   * @param e The exception to handle.
   * @return ResponseEntity with the appropriate error details.
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
    return new ResponseEntity<>(new WebError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()),
        HttpStatus.UNAUTHORIZED);
  }

  /**
   * Handles general exceptions of type {@link java.lang.Exception}.
   *
   * @param e The exception to handle.
   * @return ResponseEntity with the appropriate error details.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleException(Exception e) {
    return new ResponseEntity<>(new WebError(HttpStatus.UNAUTHORIZED.value(), "Invalid data"),
        HttpStatus.UNAUTHORIZED);
  }

  /**
   * Handles the {@link org.springframework.http.converter.HttpMessageNotReadableException}.
   *
   * @param ex The exception to handle.
   * @return ResponseEntity with the appropriate error details.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
    return new ResponseEntity<>(new WebError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles the {@link org.springframework.web.bind.MethodArgumentNotValidException}.
   *
   * @param ex The exception to handle.
   * @return ResponseEntity with the appropriate error details.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleHttpMessageNotReadable(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .toList();
    return new ResponseEntity<>(new WebError(HttpStatus.BAD_REQUEST.value(), errors.toString()),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles the {@link java.lang.IllegalStateException}.
   *
   * @param ex The exception to handle.
   * @return ResponseEntity with the appropriate error details.
   */
  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<?> handleIllegalStateException(IllegalStateException ex) {
    return new ResponseEntity<>(new WebError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles the {@link org.springframework.web.servlet.NoHandlerFoundException}.
   *
   * @param ex The exception to handle.
   * @return ResponseEntity with the appropriate error details.
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<?> handleNoHandlerFoundException(NoHandlerFoundException ex) {
    return new ResponseEntity<>(new WebError(HttpStatus.NOT_FOUND.value(), ex.getMessage()),
        HttpStatus.NOT_FOUND);
  }

  /**
   * Handles the {@link io.jsonwebtoken.JwtException}.
   *
   * @param ex The exception to handle.
   * @return ResponseEntity with the appropriate error details.
   */
  @ExceptionHandler(JwtException.class)
  public ResponseEntity<?> handleJwtException(JwtException ex) {
    return new ResponseEntity<>(new WebError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles the {@link project.web.exceptions.UserAlreadyExistsException}.
   *
   * @param ex The exception to handle.
   * @return ResponseEntity with the appropriate error details.
   */
  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<?> handleUsernameAlreadyExistsException(UserAlreadyExistsException ex) {
    return new ResponseEntity<>(new WebError(HttpStatus.CONFLICT.value(), ex.getMessage()),
        HttpStatus.CONFLICT);
  }
}
