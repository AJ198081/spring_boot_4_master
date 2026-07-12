package dev.aj.bank_customer.controllers.advice;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomerExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ProblemDetail catchAllHandler(Exception exception) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle(exception.getClass().getSimpleName());
        problemDetail.setDetail(exception.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle(exception.getClass().getSimpleName());
        problemDetail.setDetail(exception.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(value = ObjectOptimisticLockingFailureException.class)
    public ProblemDetail handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle(exception.getClass().getSimpleName());
        problemDetail.setDetail(exception.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ProblemDetail handleIllegalStateException(IllegalStateException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle(exception.getClass().getSimpleName());
        problemDetail.setDetail(exception.getMessage());

        return problemDetail;
    }

}
