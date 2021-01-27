package com.macro.mall.controller;

import com.macro.mall.selfmallcommon.api.CommonResult;
import com.macro.mall.selfmallcommon.exception.BusinessException;
import com.macro.mall.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangwang
 * @date 2021/1/26 16:24
 */
@RestController
@RequestMapping("/oos")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/getOtpCode")
    public CommonResult getOtpCode(@RequestParam String telephone) throws BusinessException {
        String otpCode = memberService.getOtpCode(telephone);
        return CommonResult.success(otpCode);
    }
}
