package com.lineyk.characterchat.domain.user.event;

import java.util.UUID;

public record UserSignedUpEvent(
    UUID userId,
    String email
) {
    
}
