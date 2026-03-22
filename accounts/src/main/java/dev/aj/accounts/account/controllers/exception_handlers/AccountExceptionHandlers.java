package dev.aj.accounts.account.controllers.exception_handlers;

import dev.aj.accounts.common.exceptions.AccountAlreadyExistsException;
import dev.aj.accounts.common.exceptions.AccountNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice(basePackages = "dev.aj.accounts.account.controllers")
@Slf4j
public class AccountExceptionHandlers {

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ProblemDetail handleAccountAlreadyExistsException(AccountAlreadyExistsException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ProblemDetail handleAccountNotFoundException(AccountNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        ProblemDetail badRequestProblemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getBody()
                        .getDetail());

        // Collects field errors into problem detail properties
        Map<String, Object> fieldErrorsMap = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getObjectName() + "." + fieldError.getField(),
                        (fieldError -> Objects.requireNonNullElse(fieldError.getDefaultMessage(), "Invalid error value")),
                        (oldValue, newValue) -> (oldValue + "; " + newValue))
                );

        badRequestProblemDetail.setProperties(fieldErrorsMap);

        return badRequestProblemDetail;
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception exception) {
        log.error("Generic exception caught: {} - {}", exception.getClass().getName(), exception.getMessage(), exception);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred, contact your administrator. %s".formatted(exception.getMessage()));
    }

}
