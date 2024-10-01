package rj.com.store.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//this class is used to perform jwt operations
@Component
public class JwtHelper {
//    1. validity
    //validity in millis
    public static final long TOKEN_VALIDITY = 5*60*60*1000;
    //    2. secret key
    public static final String SECRET_KEY = "yrbwehrbweibviebrqhbrihqberhrqberheqrhvfqvqergqrifvqwfveqhfeeregreihovfqwihfehwcvdfsdwadwedwevwfewewfewaefdcXCaefawjrbeajrbaejibaiebeiawvheiwbihefvwahievawihvahivwhavhiaevefiapwvf";
    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims=getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(generalKey()).build().parseSignedClaims(token).getPayload();
    }
    public Boolean isTokenExpired(String token) {
        final Date expiration =getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
//retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
    }
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims,userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts
                .builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(generalKey())
                .compact();
    }
    public static SecretKey generalKey(){
        byte[] encodeKey = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(encodeKey);
    }
}
