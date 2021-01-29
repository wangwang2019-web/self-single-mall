package com.macro.mall.controller;

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
        return "返回数据";
    }
}
