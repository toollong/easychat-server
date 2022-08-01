package com.easychat.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * @Author: long
 * @Date: 2022-05-01 12:09
 */
public class JwtUtil {

    private static final String KEY = MD5Util.getStringMD5("easypay");

    public static String createJwt(Map<String, Object> claims, long expireTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSecretKey())
                .compact();
    }

    public static boolean verifyJwt(String jwt) {
        try {
            Jwts.parserBuilder()
                    .setAllowedClockSkewSeconds(60)
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(jwt);
        } catch (JwtException e) {
            return false;
        }
        return true;
    }

    public static Map<String, Object> parseJwt(String jwt) {
        Claims claims = Jwts.parserBuilder()
                .setAllowedClockSkewSeconds(60)
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        claims.remove("iat");
        claims.remove("exp");
        return claims;
    }

    private static SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(KEY.getBytes(StandardCharsets.UTF_8));
    }

    //    public static void main(String[] args) {
    //        Map<String, Object> map = new HashMap<>();
    //        map.put("张三", "123456");
    //        map.put("李四", "456789");
    //        String jwt = createJwt(map, 60 * 1000);
    //        System.out.println(jwt);
    //        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyLmnY7lm5siOiI0NTY3ODkiLCLlvKDkuIkiOiIxMjM0NTYiLCJpYXQiOjE2NTY3NTAxOTMsImV4cCI6MTY1Njc1MDI1M30._ue7gE7moWa2SDIeq3Wr_FIAzFpKruPIvm7JMtcI95c";
    //        boolean result = verifyJwt(jwt);
    //        if (result) {
    //            System.out.println(parseJwt(jwt).toString());
    //        }
    //    }
}
