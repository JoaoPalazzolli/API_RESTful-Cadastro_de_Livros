package br.com.listadelivros.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.com.listadelivros.data.vo.v1.security.TokenVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServices {
    
    @Value("${security.jwt.token.secret-key:default}")
    private String secretKey = "";

    @Value("${security.jwt.token.expire-length:default}")
    private long expireLength;

    public String extractUsername(String token) { 
        return extractClaim(token, Claims::getSubject); 
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { 
        final Claims claims = extractAllClaims(token); 
        return claimsResolver.apply(claims); 
    }

    private String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),  userDetails);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){ 
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + (expireLength * 3)))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256) 
            .compact();
    }

    public TokenVO createToken(UserDetails userDetails){
        return TokenVO.builder()
        .username(userDetails.getUsername())
        .authenticated(true)
        .created(new Date())
        .expiration(new Date(System.currentTimeMillis() + (expireLength * 3)))
        .accessToken(generateToken(userDetails))
        .build();
    }

    public TokenVO createToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return TokenVO.builder()
        .username(userDetails.getUsername())
        .authenticated(true)
        .created(new Date())
        .expiration(new Date(System.currentTimeMillis() + (expireLength * 3)))
        .accessToken(generateToken(extraClaims, userDetails))
        .build();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {  
        return extractExpiration(token).before(new Date()); 
    }

    private Date extractExpiration(String token) { 
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey()) 
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
