package com.lineyk.characterchat.global.payment.util;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TossCustomerKeyUtil {

    private TossCustomerKeyUtil() {}

    /**
     * 유저 ID와 Toss Secret Key를 기반으로 안전하고 고유한 customerKey를 생성합니다.
     * 토스페이먼츠 customerKey 제약: 영문 대소문자, 숫자, 특수문자(-, _, =, ., @), 2자 이상 50자 이하
     */
    public static String generateCustomerKey(UUID userId, String secretKey) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(userId.toString().getBytes(StandardCharsets.UTF_8));
            // 64자 중 토스 길이 제약(50자)을 만족하도록 앞 40자리 추출
            return HexFormat.of().formatHex(hash).substring(0, 40);
        } catch (Exception e) {
            throw new RuntimeException("customerKey 생성 실패", e);
        }
    }
}
