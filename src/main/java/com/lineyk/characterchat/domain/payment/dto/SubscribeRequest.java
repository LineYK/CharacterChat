package com.lineyk.characterchat.domain.payment.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubscribeRequest(
    @NotNull(message = "플랜 ID는 필수입니다.") UUID planId,
    @NotBlank(message = "authKey는 필수입니다.") String authKey,
    @NotBlank(message = "customerKey는 필수입니다.") String customerKey
) {
}
