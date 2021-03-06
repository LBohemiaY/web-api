package org.bohemia.webapi.utils.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.bohemia.webapi.entity.User;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Slf4j
public class TokenUtil {
    public static long EXPIRE_TIME = 60*60*1000;
    public static String TOKEN_SECRET = "BohemiaToken";
    public static String ISSUER = "Bohemia";
    /**
     * 签名生成
     * @param user
     * @return
     */
    public static String sign(User user){
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            token = JWT.create()
                    .withIssuer(ISSUER)
                    .withAudience(user.getUsername())
                    .withClaim("username",user.getUsername())
                    .withExpiresAt(expiresAt)
                    // 使用了HMAC256加密算法。
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }

    /**
     * 签名验证
     * @param token
     * @return
     */
    public static boolean verify(String token){
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer(ISSUER).build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("认证通过：");
            System.out.println("issuer: " + jwt.getIssuer());
            System.out.println("username: " + jwt.getClaim("username").asString());
            System.out.println("过期时间: " + jwt.getExpiresAt());
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public static String getClaimUsername(String token){
        try{
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer(ISSUER).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("username").asString();
        }catch (Exception e){
            log.info("Error in TokenUtil getClainUsername: ",e);
            return null;
        }
    }

}
