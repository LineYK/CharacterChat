package com.lineyk.characterchat.domain.chat.dto;

public record AffinityData(
    long score,
    long nextThreshold,
    boolean datingAvailable
) {
    
}
