package com.macro.mall.service;

import com.macro.mall.selfmallcommon.exception.BusinessException;

/**
 * @author wangwang
 * @date 2021/1/26 17:14
 */
public interface MemberService {
    /**
     *
     * @param telePhone
     * @return
     */
    public String getOtpCode(String telePhone) throws BusinessException;
}
