package com.macro.mall.controller;

import com.macro.mall.interceptor.AuthInterceptorHandler;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangwang
 * @date 2021/1/28 17:17
 */
@Slf4j
@RestController
public class OrderController extends BaseController{

    @GetMapping("/order")
    public String order(){
        Claims claims=(Claims) getRequest().getAttribute(AuthInterceptorHandler.CLOBAL_JWT_USER_INFO);
        log.info(String.format("下订单->8888,user:%s",claims.get("sub")));

        return String.format("下订单->8888,user:%s",claims.get("sub"));
    }
}
