package com.allianceoneapparel.core.common;

import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseAPI<T> {
    private int code;
    @Nullable private String message;
    private T data;
}
