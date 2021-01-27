package com.macro.mall.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wangwang
 * @date 2021/1/27 16:24
 */
@ConfigurationProperties(prefix = "redis.key")
@Data
public class RedisKeyPrefixConfig {
    private RedisKeyPrefixConfig.Prefix prefix;

    private RedisKeyPrefixConfig.Expire expire;

    @Data
    public static class Prefix{
        private String otpCode;
    }

    @Data
    public static class Expire{
        private Long otpCode;
    }

}
