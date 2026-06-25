package dev.aj.order_service.controller.advice;

import dev.aj.order_service.model.exception.ApplicationException;
import dev.aj.order_service.model.exception.DomainError;
import dev.aj.order_service.model.exception.SystemError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ProblemDetail handleApplicationException(ApplicationException exception) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(switch (exception.getError()) {

            case DomainError.EntityNotFound _ -> HttpStatus.NOT_FOUND;
            case DomainError.EntityAlreadyExists _ -> HttpStatus.CONFLICT;
            case DomainError.PaymentFailed _ -> HttpStatus.PAYMENT_REQUIRED;
            case DomainError.MissingArgument _ -> HttpStatus.BAD_REQUEST;
            case DomainError.ProductDiscontinued _ -> HttpStatus.GONE;
            case DomainError.InvalidResponseBody _ -> HttpStatus.PAYMENT_REQUIRED;

            case SystemError _ -> HttpStatus.INTERNAL_SERVER_ERROR;
        });

        problemDetail.setTitle(exception.getError().getClass().getSimpleName());

        problemDetail.setDetail(switch (exception.getError()){
            case DomainError _ -> "Client made an exception and is non-recoverable.";

            case SystemError systemError ->
                    "System exception while processing the request with %s".formatted(systemError.service());
        });

        return problemDetail;
    }

}
