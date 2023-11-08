package com.allianceoneapparel.core.exception;

import com.allianceoneapparel.core.Constants;
import com.allianceoneapparel.core.extension.MessageSourceExtensions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalException {
    private final MessageSourceExtensions localize;

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<AppException> handleAppException(Exception exception) {
        log.error(exception.getMessage());
        var error = new AppException(500, localize.getMsg(Constants.SYSTEM_ERROR));
        return ResponseEntity.status(500).body(error);
    }
}
