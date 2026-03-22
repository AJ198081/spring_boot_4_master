package dev.aj.accounts.customer.controllers.exception_handlers;

import dev.aj.accounts.common.exceptions.CustomerAlreadyExistsException;
import dev.aj.accounts.common.exceptions.CustomerNotFoundException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomerExceptionHandlers {

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    @ApiResponse(
            responseCode = "409",
            description = "Customer Already Exists",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class))
    )
    public ProblemDetail handleCustomerAlreadyExistsException(CustomerAlreadyExistsException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ApiResponse(
            responseCode = "404",
            description = "Customer Not Found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class))
    )
    public ProblemDetail handleCustomerNotFoundException(CustomerNotFoundException exception) {
        log.error("CustomerNotFoundException caught: {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class))
    )
    public ProblemDetail handleGenericException(Exception exception) {
        log.error("Generic exception caught: {} - {}", exception.getClass().getName(), exception.getMessage(), exception);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred, contact your administrator. %s".formatted(exception.getMessage()));
    }

}
