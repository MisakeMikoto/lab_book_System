package com.yiling.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yiling.domain.User;

import java.util.Date;

/**
 * @Author MisakiMikoto
 * @Date 2023/3/31 23:01
 */
public class TokenUtils {

    //Token到期时间
    private static final long EXPIRE_TIME = 24*60*60*1000;

    //密钥盐
    private static final String TOKEN_SECRET="ljdyaishijin**3nkjnj??";


    public String initToken(User user) {
        String token = null;
        System.out.println(user);
        try {
            Date expireAt=new Date(System.currentTimeMillis()+EXPIRE_TIME);
            token = JWT.create()
                    //发行人
                    .withIssuer("auth0")
                    //存放数据
                    .withClaim("userId",user.getUserId().toString())
                    //过期时间
                    .withExpiresAt(expireAt)
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (Exception e) {
            throw e;
        }
        return token;

    }

    /**
     * token验证
     * @param token
     * @return
     */
    public String verify(String token){
        String userId = null;
        try {
            //创建token验证器
            JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT decodedJWT=jwtVerifier.verify(token);
            userId = decodedJWT.getClaim("userId").asString();
//            System.out.println("initToken userId ="+ userId);
//            System.out.println("认证通过：");
//            System.out.println("username: " + decodedJWT.getClaim("username").asString());
//            System.out.println("过期时间：      " + decodedJWT.getExpiresAt());
        } catch (IllegalArgumentException | JWTVerificationException e) {
            //抛出错误即为验证不通过
            return null;
        }
        return userId;
    }

}
