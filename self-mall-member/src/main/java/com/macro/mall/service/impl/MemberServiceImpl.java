package com.macro.mall.service.impl;

import com.macro.mall.config.RedisConfiguration;
import com.macro.mall.config.properties.RedisKeyPrefixConfig;
import com.macro.mall.domain.UmsMember;
import com.macro.mall.domain.UmsMemberExample;
import com.macro.mall.mapper.UmsMemberMapper;
import com.macro.mall.selfmallcommon.exception.BusinessException;
import com.macro.mall.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author wangwang
 * @date 2021/1/26 17:15
 */
@Slf4j
@EnableConfigurationProperties(value = RedisKeyPrefixConfig.class)
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisKeyPrefixConfig redisKeyPrefixConfig;

    @Override
    public String getOtpCode(String telePhone) throws BusinessException {
        //1.查询当前用户是否有注册
        UmsMemberExample example=new UmsMemberExample();
        example.createCriteria().andPhoneEqualTo(telePhone);
        List<UmsMember> results = umsMemberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(results)){
            throw new BusinessException("已经注册过了");
        }

        //2.校验60s内有没有发送过
        if (redisTemplate.hasKey(redisKeyPrefixConfig.getPrefix().getOtpCode()+telePhone)){
            throw new BusinessException("请60s后再试！");
        }

        //3.生成随即校验码
        Random random=new Random();
        StringBuilder stb=new StringBuilder();
        for (int i=0;i<6;i++){
            stb.append(random.nextInt(10));
        }
        log.info("otpCode={}",stb.toString());
        redisTemplate.opsForValue().set(redisKeyPrefixConfig.getPrefix().getOtpCode()+telePhone,stb.toString()
                ,redisKeyPrefixConfig.getExpire().getOtpCode(), TimeUnit.SECONDS);

        return stb.toString();
    }
}
