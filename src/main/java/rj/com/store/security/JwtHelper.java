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
    /**
     * Retrieves the username from the provided JWT token.
     *
     * @param token the JWT token
     * @return the username contained in the token
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    /**
     * Retrieves a specific claim from the JWT token.
     *
     * @param token the JWT token
     * @param claimsResolver a function to extract the desired claim
     * @param <T> the type of the claim
     * @return the value of the specified claim
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims=getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    /**
     * Retrieves all claims from the provided JWT token.
     *
     * @param token the JWT token
     * @return the claims contained in the token
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(generalKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    /**
     * Checks if the JWT token has expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration =getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    /**
     * Retrieves the expiration date from the provided JWT token.
     *
     * @param token the JWT token
     * @return the expiration date
     */
    public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
    }
    /**
     * Generates a JWT token for the specified user details.
     *
     * @param userDetails the user details for whom the token is generated
     * @return the generated JWT token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims,userDetails.getUsername());
    }
    /**
     * Creates a JWT token using the specified claims and subject.
     *
     * @param claims the claims to include in the token
     * @param subject the subject for whom the token is generated (usually the username)
     * @return the generated JWT token
     */
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
    /**
     * Generates a secret key for signing the JWT tokens.
     *
     * @return the secret key used for signing
     */
    public static SecretKey generalKey(){
        byte[] encodeKey = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(encodeKey);
    }
}
