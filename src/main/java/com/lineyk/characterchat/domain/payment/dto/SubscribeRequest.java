package com.lineyk.characterchat.domain.payment.dto;

import java.util.UUID;

public record SubscribeRequest(
    UUID planId,
    String authKey,
    String customerKey
) {

}
