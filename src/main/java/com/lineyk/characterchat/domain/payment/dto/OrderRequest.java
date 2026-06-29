package com.lineyk.characterchat.domain.payment.dto;

import java.util.UUID;

public record OrderRequest(
    UUID packageId
) {}
