package ru.mts.customerservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.mts.customerservice.exception.CustomException;
import ru.mts.customerservice.model.ExceptionData;
import ru.mts.customerservice.model.ExceptionResponse;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse<ExceptionData>> handleException(CustomException e) {
        ExceptionResponse<ExceptionData> exceptionResponse =
                new ExceptionResponse<>(new ExceptionData(e.getCode(), e.getMessage()));

        log.error("Произошла ошибка: {}, Код ошибки: {}, Сообщение ошибки: {}",
                e.getClass().getSimpleName(),
                e.getCode(),
                e.getMessage()
        );

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse<ExceptionData>> handleException(BadCredentialsException e) {
        ExceptionResponse<ExceptionData> exceptionResponse =
                new ExceptionResponse<>(new ExceptionData("AUTH_ERROR", "Неверный логин или пароль"));

        log.error("Произошла ошибка: {}, Код ошибки: {}, Сообщение ошибки: {}",
                e.getClass().getSimpleName(),
                "AUTH_ERROR",
                e.getMessage()
        );

        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }
}
