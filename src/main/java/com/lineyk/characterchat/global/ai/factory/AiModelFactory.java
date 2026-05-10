package com.lineyk.characterchat.global.ai.factory;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lineyk.characterchat.global.ai.constant.AiProvider;
import com.lineyk.characterchat.global.ai.strategy.AiModelStrategy;
import com.lineyk.characterchat.global.error.CustomException;
import com.lineyk.characterchat.global.error.ErrorCode;

@Component
public class AiModelFactory {

    private final Map<AiProvider, AiModelStrategy> strategies;

    public AiModelFactory(List<AiModelStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(
                    AiModelStrategy::provider, 
                    Function.identity(),
                    (existing, replacement) -> existing, 
                    () -> new EnumMap<>(AiProvider.class)
                ));
    }

    public AiModelStrategy getStrategy(AiProvider provider) {
        AiModelStrategy strategy = strategies.get(provider);
        if (strategy == null) {
            throw new CustomException(ErrorCode.AI_PROVIDER_NOT_FOUND);
        }
        return strategy;
    }
}
