package com.car.carshowroombackend.utill;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for handling JSON Web Token (JWT) operations, such as generating,
 * validating, and extracting information from tokens.
 */
@Component
public class JwtUtil {

    /**
     * Generates a JWT with specified claims, a subject (username), and an expiration time.
     *
     * @param extraClaims Additional claims to be included in the token.
     * @param details     UserDetails object containing the user's information.
     * @return A signed JWT string.
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails details) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(details.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Token valid for 24 hours
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Overloaded method to generate a JWT without additional claims.
     *
     * @param userDetails UserDetails object containing the user's information.
     * @return A signed JWT string.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Validates a JWT by comparing the token's username with the provided user's username
     * and checking if the token is expired.
     *
     * @param token       The JWT string to validate.
     * @param userDetails UserDetails object containing the user's information.
     * @return true if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Extracts all claims from the JWT.
     *
     * @param token The JWT string.
     * @return Claims object containing all the claims in the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extracts the username (subject) from the token.
     *
     * @param token The JWT string.
     * @return The username as a string.
     */
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the token.
     *
     * @param token The JWT string.
     * @return The expiration date of the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Checks if the token is expired by comparing the expiration date with the current date.
     *
     * @param token The JWT string.
     * @return true if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts a specific claim from the token using a provided function.
     *
     * @param token          The JWT string.
     * @param claimsResolver Function to resolve and retrieve a specific claim.
     * @param <T>            The type of the claim.
     * @return The claim value.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Retrieves the signing key used to sign the JWT, decoded from a base64-encoded string.
     *
     * @return The signing key.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode("413F4428472B4B6250655368566D5970337336763979244226452948404D6351");
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
