package com.macro.mall.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * @author wangwang
 * @date 2021/1/28 15:58
 */
@Data
@ConfigurationProperties(prefix = "member.auth")
public class NoAuthUrlProperties {
    private LinkedHashSet<String> shouldSkipUrls;
}
