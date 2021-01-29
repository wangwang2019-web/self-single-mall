package com.macro.mall.controller;

import com.macro.mall.domain.Register;
import com.macro.mall.domain.UmsMember;
import com.macro.mall.selfmallcommon.api.CommonResult;
import com.macro.mall.selfmallcommon.exception.BusinessException;
import com.macro.mall.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author wangwang
 * @date 2021/1/26 16:24
 */
@RestController
@RequestMapping("/oos")
public class MemberController extends BaseController{

    @Autowired
    private MemberService memberService;

    @PostMapping("/getOtpCode")
    public CommonResult getOtpCode(@RequestBody String telephone) throws BusinessException {
        String otpCode = memberService.getOtpCode(telephone);
        return CommonResult.success(otpCode);
    }

    @PostMapping("/register")
    public CommonResult register(@Valid @RequestBody Register register) throws Exception{
        int result = memberService.register(register);
        if (result>0){
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @PostMapping("/login")
    public CommonResult login(@RequestParam String username,@RequestParam String password) throws BusinessException {
        UmsMember member=memberService.login(username, password);
        if (member!=null){
            getHttpSession().setAttribute("member",member);
            getHttpSession().getAttribute("member");//redisSession
            return CommonResult.success(username+"登陆成功");
        }
        return CommonResult.failed();
    }

}
