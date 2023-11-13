package com.allianceoneapparel.authentication;

import com.allianceoneapparel.core.common.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record AuthenticationRequest(
        @NotEmpty(message = Constants.AUTH_ERROR)
        String username,
        @NotEmpty(message = Constants.AUTH_ERROR)
        String password
) {
}
