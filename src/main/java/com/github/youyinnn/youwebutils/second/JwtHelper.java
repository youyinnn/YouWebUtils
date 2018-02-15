package com.github.youyinnn.youwebutils.second;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/**
 * @author youyinnn
 */
public class JwtHelper {

    private static Algorithm alg;

    private static String issuer;

    private JWTCreator.Builder builder;

    public static void initJWTWithHMAC256(String _issuer, String secret) {
        issuer = _issuer;
        try {
            alg = Algorithm.HMAC256(secret);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void initJWTWithHMAC512(String _issuer, String secret) {
        issuer = _issuer;
        try {
            alg = Algorithm.HMAC512(secret);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String getRawHeader(String token){
        DecodedJWT tokenDecode = JWT.decode(token);
        String header = tokenDecode.getHeader();
        byte[] decode = Base64.getDecoder().decode(header);
        return new String(decode);
    }

    public static String getRawPayLoad(String token){
        DecodedJWT tokenDecode = JWT.decode(token);
        String payload = tokenDecode.getPayload();
        byte[] decode = Base64.getDecoder().decode(payload);
        return new String(decode);
    }

    public static boolean verify(String token){
        JWTVerifier jwtVerifier = JWT.require(alg).withIssuer(issuer).build();
        try {
            jwtVerifier.verify(token);
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

    public JwtHelper(){
        if (issuer == null) {
            initJWTWithHMAC256("admin","secret000");
        }
        builder = JWT.create().withIssuer(issuer);
    }

    public JwtHelper setClaim(String name, Object value) {
        if (value instanceof String) {
            builder.withClaim(name, (String)value);
        } else if (value instanceof Date) {
            builder.withClaim(name, (Date) value);
        } else if (value instanceof Integer) {
            builder.withClaim(name, (Integer) value);
        } else if (value instanceof Double) {
            builder.withClaim(name, (Double) value);
        } else if (value instanceof Long) {
            builder.withClaim(name, (Long) value);
        } else if (value instanceof Boolean) {
            builder.withClaim(name, (Boolean) value);
        }
        return this;
    }

    public JwtHelper setAudience(String ... audience) {
        builder.withAudience(audience);
        return this;
    }

    public JwtHelper setSubject(String subject){
        builder.withSubject(subject);
        return this;
    }

    public JwtHelper setExpiration(Date date){
        builder.withExpiresAt(date);
        return this;
    }

    public JwtHelper setNotBefore(Date date){
        builder.withNotBefore(date);
        return this;
    }

    public JwtHelper setIssuedAt(Date date){
        builder.withIssuedAt(date);
        return this;
    }

    public String getToken(){
        return builder.sign(alg);
    }

    public static String getClaimAsString(String token, String name) {
        DecodedJWT decode = JWT.decode(token);
        return decode.getClaim(name).asString();
    }

    public static Boolean getClaimAsBoolean(String token, String name) {
        DecodedJWT decode = JWT.decode(token);
        return decode.getClaim(name).asBoolean();
    }

    public static Date getClaimAsDate(String token, String name) {
        DecodedJWT decode = JWT.decode(token);
        return decode.getClaim(name).asDate();
    }

    public static Double getClaimAsDouble(String token, String name) {
        DecodedJWT decode = JWT.decode(token);
        return decode.getClaim(name).asDouble();
    }

    public static Integer getClaimAsInteger(String token, String name) {
        DecodedJWT decode = JWT.decode(token);
        return decode.getClaim(name).asInt();
    }
    public static Long getClaimAsLong(String token, String name) {
        DecodedJWT decode = JWT.decode(token);
        return decode.getClaim(name).asLong();
    }

    public static Map<String, Claim> getClaims(String token) {
        DecodedJWT decode = JWT.decode(token);
        return decode.getClaims();
    }

}
