package com.allianceoneapparel.core.exception;

import com.allianceoneapparel.core.common.Constants;
import com.allianceoneapparel.core.common.ResponseAPI;
import com.allianceoneapparel.core.extension.MessageSourceExtensions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalException {
    private final MessageSourceExtensions localize;

    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<ResponseAPI<Object>> handleException(Exception exception) {
        log.error(exception.getMessage());
        exception.printStackTrace();
        String msg = localize.getMsg(Constants.SYSTEM_ERROR);
        var response = new ResponseAPI<>(500, msg, null);
        return ResponseEntity.status(500).body(response);
    }



    @ExceptionHandler(AppException.class)
    public ResponseEntity<ResponseAPI<Object>> handleAppException(AppException e) {
        log.error(e.getLocalizedMessage());
        String msg = (e.getMessage() == null) ? localize.getMsg(Constants.SYSTEM_ERROR) : e.getMessage();
        var response = new ResponseAPI<>(e.getCode(), msg, null);
        return ResponseEntity.status(e.getCode()).body(response);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseAPI<Object>> handleBindException(BindException e) {
        var exMsg = e.getAllErrors().get(0).getDefaultMessage();
        var error = localize.getMsg(exMsg);
        var response = new ResponseAPI<>(400, error, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ResponseAPI<Object>> handleAuthentication(AuthenticationException e) {
        var response = new ResponseAPI<>(403, localize.getMsg(Constants.AUTH_ERROR), null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
