package com.allianceoneapparel.core.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class AppException extends RuntimeException {
    private int code;
    private String message;
}



//public class Response<T> {}