package ma.ensa.ebanking.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // signing key
    private static final String
            SECRET_KEY = "70655367566B5970347336763979244226452948404D635166546A576D5A7164";

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // parse token

    public Date extractExpiration(String jwt){
        return extractClaim(jwt, Claims::getExpiration);

    }

    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwt){

            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
    }

    // generate token

    public String generateToken(UserDetails userDetails){
        return generateToken(
                Collections.emptyMap(), userDetails
        );
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){
        final long CURRENT_MILLIS = System.currentTimeMillis(),
                MONTH_TO_MILLIS = 1000 * 60 * 60 * 24 * 30L;
        final Date
                currentDate = new Date(CURRENT_MILLIS),
                expirationdDate = new Date(CURRENT_MILLIS + MONTH_TO_MILLIS );
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(currentDate)
                .setExpiration(expirationdDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // verify the token
    public boolean isTokenValid(
            String jwt,
            UserDetails userDetails)
    {
        // TODO: verify the signature
        final String username = extractUsername(jwt);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(jwt);
    }


    private boolean isTokenExpired(String token){
        final Date currentDate = new Date(
                System.currentTimeMillis()
        );
        return extractExpiration(token).before(currentDate);
    }

}
