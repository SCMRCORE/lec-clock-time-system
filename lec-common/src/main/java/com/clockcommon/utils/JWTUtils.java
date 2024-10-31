package com.clockcommon.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JWTUtils {
    private static final String SING = "%!@@QWFH124!@512GWQKY";

    public static String createJWT(Map<String, String> tokenMap, int time){
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, time);
        JWTCreator.Builder builder = JWT.create().withExpiresAt(instance.getTime());
        for(String key : tokenMap.keySet()){
            String val = tokenMap.get(key);
            builder.withClaim(key, val);
        }
        return builder.sign(Algorithm.HMAC256(SING));
    }

    public static String createJWT(Long id, int time){
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, time);
        JWTCreator.Builder builder = JWT.create().withExpiresAt(instance.getTime());
        builder.withClaim("userId", id);
        return builder.sign(Algorithm.HMAC256(SING));
    }

    public static String createJWT(Map<String, String> tokenMap){
        return createJWT(tokenMap, 60 * 60 * 24*7);
    }

    public static String createJWT(Long id){
        return createJWT(id, 60 * 60 * 24*7);
    }

    public static String verifyJWT(String token, String o) {
        DecodedJWT jwt = parseJWT(token);
        return jwt.getClaim(o).asString();
    }

    public static Long getSubject(String token) throws TokenExpiredException {
        DecodedJWT jwt = parseJWT(token);
        return jwt.getClaim("userId").asLong();
    }

    public static DecodedJWT parseJWT(String token) throws TokenExpiredException {
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }
}