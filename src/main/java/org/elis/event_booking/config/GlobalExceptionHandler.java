package org.elis.event_booking.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.elis.event_booking.dto.ErrorDTO;
import org.elis.event_booking.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// global exceptions handling
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(EntityNotFoundException exception, WebRequest webRequest) {
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.NOT_FOUND, exception.getMessage(), ((ServletWebRequest)webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            DifferentPlaceException.class,
            BookingNotAllowedException.class,
            BookingDisableNotAllowedException.class,
            EventDateOverlappingException.class,
            ReviewWithoutBookingException.class,
            ReviewBeforeEndException.class,
            SeatEditPriceNotAllowedException.class,
            HttpMessageConversionException.class
    })
    public ResponseEntity<ErrorDTO> handleBadRequestException(Exception exception, WebRequest webRequest) {
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.BAD_REQUEST, exception.getMessage(), ((ServletWebRequest)webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationException(MethodArgumentNotValidException exception, WebRequest webRequest) {
        Map<String, String> errorMap = new HashMap<>();

        // getFieldsErrors() returns all fields that have validation errors
        exception.getBindingResult().getFieldErrors().forEach(
                error -> errorMap.put(error.getField(), error.getDefaultMessage())
        );

        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.BAD_REQUEST, errorMap, ((ServletWebRequest)webRequest).getRequest().getRequestURI());

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            EntityCreationException.class,
            EntityDeletionException.class,
            EntityEditException.class,
            UserEditActiveException.class,
            EmailSendingException.class
    })
    public ResponseEntity<ErrorDTO> handleBadGatewayException(RuntimeException exception, WebRequest webRequest) {
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.BAD_GATEWAY, exception.getMessage(), ((ServletWebRequest)webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler({
            InvalidCredentialsException.class,
            ExpiredJwtException.class,
            SignatureException.class
    })
    public ResponseEntity<ErrorDTO> handleUnauthorizedException(RuntimeException exception, WebRequest webRequest) {
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.UNAUTHORIZED, exception.getMessage(), ((ServletWebRequest)webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
            UserNotActiveException.class,
            AccessDeniedException.class,
            AuthenticationException.class
    })
    public ResponseEntity<ErrorDTO> handleForbiddenException(RuntimeException exception, WebRequest webRequest) {
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.FORBIDDEN, exception.getMessage(), ((ServletWebRequest)webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(errorDTO, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityDuplicateException.class)
    public ResponseEntity<ErrorDTO> handleConflictException(EntityDuplicateException exception, WebRequest webRequest) {
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.CONFLICT, exception.getMessage(), ((ServletWebRequest)webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(errorDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SQLException.class)
    private ResponseEntity<ErrorDTO> handleInternalServerException(Exception exception, WebRequest webRequest) {
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), ((ServletWebRequest)webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorDTO buildErrorDTO(HttpStatus httpStatus, Object message, String path) {
        return new ErrorDTO(LocalDateTime.now(), httpStatus.value(), httpStatus.getReasonPhrase(), message, path);
    }
}