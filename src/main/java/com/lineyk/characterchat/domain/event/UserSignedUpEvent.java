package com.lineyk.characterchat.domain.event;

import java.util.UUID;

public record UserSignedUpEvent(
    UUID userId,
    String email
) {
    
}
