package com.macro.mall.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Random;

/**
 * @author wangwang
 * @date 2021/1/28 10:09
 */
@Data
public class Register {

    @NotBlank(message = "用户名不能为空")
    @Length(max = 20,min = 4,message = "用户名长度需在4-20个字符之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(max = 20,min = 8,message = "密码长度需在8-20个字符之间")
    private String password;

    @NotBlank(message = "手机号码不能为空")
    @Length(max = 11,min = 11,message = "手机号码长度必须是11个数字")
    @Pattern(regexp = "^1[3|4|5|8][0-9]\\d{8}$",message = "不符合手机号要求")
    private String phone;

    @NotBlank(message = "校验码不能为空")
    @Length(max = 6,min = 6,message = "校验码长度必须是6个字符")
    private String otpCode;

    public static String main(String[] args) {
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<6;i++){
            int ran = random.nextInt(10);
            sb.append(ran);
        }

        return sb.toString();
    }
}
