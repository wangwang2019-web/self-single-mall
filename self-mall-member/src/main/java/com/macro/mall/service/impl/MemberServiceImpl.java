package com.macro.mall.service.impl;

import com.macro.mall.config.RedisConfiguration;
import com.macro.mall.config.properties.RedisKeyPrefixConfig;
import com.macro.mall.domain.Register;
import com.macro.mall.domain.UmsMember;
import com.macro.mall.domain.UmsMemberExample;
import com.macro.mall.mapper.UmsMemberMapper;
import com.macro.mall.selfmallcommon.api.CommonResult;
import com.macro.mall.selfmallcommon.exception.BusinessException;
import com.macro.mall.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

    private Logger logger= LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired(required = false)
    private RedisKeyPrefixConfig redisKeyPrefixConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public int register(Register register) throws Exception{
        String redisOptCode=(String)redisTemplate.opsForValue().get(redisKeyPrefixConfig.getPrefix().getOtpCode()+register.getPhone());
        logger.info("otpCode:{}",register.getOtpCode());
        logger.info("redisOptCode:{}",redisOptCode);
        if (StringUtils.isEmpty(redisOptCode) || !redisOptCode.equals(register.getOtpCode())){
            throw new Exception("校验码错误或者已失效");
        }
        UmsMember umsMember=new UmsMember();
        BeanUtils.copyProperties(register,umsMember);
        umsMember.setMemberLevelId(4l);
        umsMember.setStatus(1);
        umsMember.setPassword(passwordEncoder.encode(register.getPassword()));
        int result=0;
        try{
            result = umsMemberMapper.insertSelective(umsMember);
        }catch (Exception e){
            logger.info("错误信息：{}",e);
        }
        return result;
    }

    public UmsMember login(String username,String password) throws BusinessException {
        UmsMemberExample example=new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo(username).andStatusEqualTo(1);
        List<UmsMember> umsMembers = umsMemberMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(umsMembers)){
            throw new BusinessException("用户名或密码不正确");
        }
        if (umsMembers.size() > 1){
            throw new BusinessException("用户名被注册过多次，请联系客服");
        }
        UmsMember umsMember=umsMembers.get(0);
        if (!passwordEncoder.matches(password,umsMember.getPassword())){
            throw new BusinessException("用户名或密码不正确");
        }
        return umsMember;
    }
}
