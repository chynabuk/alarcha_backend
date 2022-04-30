package com.project.alarcha.util;

import com.project.alarcha.exception.ApiErrorException;
import com.project.alarcha.exception.ApiFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

//import static java.awt.Container.log;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = ApiFailException.class)
    public ResponseEntity<ResponseMessage<String>> handlerFailException(ApiFailException apiFailException) {
        ResponseMessage<String> failResponseMessage =
                new ResponseMessage<String>().prepareFailMessage(apiFailException.getMessage());
        String throwClassName = apiFailException.getStackTrace()[0].getClassName();
        log.warn(throwClassName + " : " + apiFailException.getMessage());
        return ResponseEntity.badRequest().body(failResponseMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessage<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException argumentNotValidException) {

        Map<String, String> details = getValidationExceptionDetails(argumentNotValidException);

        return ResponseEntity
                .badRequest()
                .body(
                        new ResponseMessage<Map<String, String>>().prepareFailMessage(details)
                );
    }

    @ExceptionHandler(value = ApiErrorException.class)
    public ResponseEntity<ResponseMessage<String>> handlerErrorException(ApiErrorException apiErrorException) {
        ResponseMessage<String> errorResponseMessage =
                new ResponseMessage<String>().prepareErrorMessage(apiErrorException.getMessage());
        String throwClassName = apiErrorException.getStackTrace()[0].getClassName();
        log.error(throwClassName + " : " + apiErrorException.getMessage());
        return ResponseEntity.internalServerError().body(errorResponseMessage);
    }

    private Map<String, String> getValidationExceptionDetails(
            MethodArgumentNotValidException argumentNotValidException) {

        Map<String, String> errors = new HashMap<>();
        argumentNotValidException
                .getBindingResult()
                .getAllErrors()
                .forEach((error) -> {
                    errors.put(((FieldError) error).getField(), error.getDefaultMessage());
                    log.warn(error.getDefaultMessage());
                });

        return errors;
    }
}