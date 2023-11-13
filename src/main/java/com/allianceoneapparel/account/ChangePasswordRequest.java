package com.allianceoneapparel.account;

import lombok.Builder;

@Builder
public record ChangePasswordRequest(
        String currentPassword,
        String newPassword,
        String confirmationPassword
) {
}
