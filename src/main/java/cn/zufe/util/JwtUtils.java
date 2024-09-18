package cn.zufe.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * jwt工具类
 */
public class JwtUtils {

    //有效时间：一小时
    private static final long EXPIRE = 60 * 60 * 1000;

    //密钥明文
    private static final String SECRET = "secret";

    //生成唯一标识
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成jwt令牌
     * @param subject
     * @param expire
     * @return
     */
    public static String createJWT(String subject, Long expire){
        JwtBuilder builder = getJwtBuilder(subject, expire, getUUID());
        return builder.compact();
    }

    /**
     * 生成jwt令牌
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis){
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);
        return builder.compact();
    }

    /**
     * 生成jwt令牌的逻辑代码
     * @param subject
     * @param expire
     * @param uuid
     * @return
     */
    private static JwtBuilder getJwtBuilder(String subject, Long expire, String uuid){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMills = System.currentTimeMillis();
        Date now = new Date(nowMills);
        if(expire == null){
            expire = JwtUtils.EXPIRE;
        }
        long expMills = nowMills + expire;
        Date expData = new Date(expMills);
        return Jwts.builder()
                .setId(uuid)                    // 设置唯一id
                .setSubject(subject)            // 设置主题
                .setIssuer("zufe")                // 设置签发者
                .setIssuedAt(now)               // 设置签发时间
                .signWith(signatureAlgorithm, secretKey) // 设置HS256对称加密算法签名，第二个参数为密钥
                .setExpiration(expData);        // 设置过期时间
    }

    /**
     * 生成签名密钥
     * @return
     */
    public static SecretKey generalKey(){
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtils.SECRET);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES"); // 生成签名密钥
    }

    /**
     * 解密
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception{
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }


}
