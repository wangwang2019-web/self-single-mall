package com.macro.mall.util;

import com.macro.mall.config.properties.JwtProperties;
import com.macro.mall.domain.UmsMember;
import com.macro.mall.selfmallcommon.exception.BusinessException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwang
 * @date 2021/1/30 9:41
 */
public class JwtKit {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 创建jwttoken
     * @param umsMember
     * @return
     */
    public String generateJwtToken(UmsMember umsMember){
        Map<String,Object> claim=new HashMap<>();
        claim.put("sub",umsMember.getUsername());
        claim.put("createdate",new Date());
        claim.put("id",umsMember.getId());
        claim.put("memberLevelId",umsMember.getMemberLevelId());

        return Jwts.builder()
                .setClaims(claim)
                .setExpiration(new Date(System.currentTimeMillis()+jwtProperties.getExpiration()*1000))
                .signWith(SignatureAlgorithm.ES256,jwtProperties.getSecret())
                .compact();
    }

    public Claims parseJwtToken(String jwtToken) throws Exception{
        Claims claims=null;
        try{
            claims=Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(jwtToken)
                    .getBody();
        }catch (ExpiredJwtException e){
            throw new BusinessException("JWT验证失败:token已经过期");

        }catch (UnsupportedJwtException e){
            throw new BusinessException("JWT验证失败:token格式不支持");
        }catch (MalformedJwtException e) {
            throw new BusinessException("JWT验证失败:无效的token");
        } catch (SignatureException e) {
            throw new BusinessException("JWT验证失败:无效的token");
        } catch (IllegalArgumentException e) {
            throw new BusinessException("JWT验证失败:无效的token");
        }
        return claims;
    }
}
