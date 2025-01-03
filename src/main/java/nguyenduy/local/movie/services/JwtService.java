package nguyenduy.local.movie.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;
import nguyenduy.local.movie.config.JwtConfig;
import nguyenduy.local.movie.helper.JwtAuthFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final JwtConfig jwtConfig;
  private final Key key;
  private static final Logger logger = LoggerFactory.getLogger(JwtService.class);


  public JwtService(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
    this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(jwtConfig.getSecretKey().getBytes()));
  }

  public String generateToken(Long userId, String phoneNumber) {
    Date now = new Date();
    Date expriedDate = new Date(now.getTime() + jwtConfig.getExpiration());

    return Jwts.builder()
        .setSubject(String.valueOf(userId))
        .claim("username", phoneNumber)
        .setIssuer(jwtConfig.getIssuer())
        .setIssuedAt(now)
        .setExpiration(expriedDate)
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();
  }

  public String getUsernameFromJwt(String token) {
    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    return claims.get("username", String.class);
  }

//  public String extractPhoneNumber(String token) {
//    return extractClaim(token, Claims::getSubject);
//  }
//

//

  public String getUserIdFromJwt(String token) {
    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    return claims.getSubject();
  }

  public boolean isValidToken(String token, UserDetails userDetails) {

    try {
      if(!isTokenFormatValid(token)) {
        logger.error("Token không đúng định dạng");
        return false;
      }

      if(!isSignatureValid(token)) {
        logger.error("Chữ ký token không hợp lệ");
        return false;
      }

      if(!isTokenExpired(token)) {
        logger.error("Token đã hết hạn");
        return false;
      }

      if(!isIssuerToken(token)) {
        logger.error("Nguồn gốc của token không hợp lệ");
        return false;
      }

      final String usernameFromToken = getUsernameFromJwt(token);
      if(!usernameFromToken.equals(userDetails.getUsername())) {
        logger.error("Token không hợp lệ");
        return false;
      }
    } catch (Exception e) {
      logger.error("Xác thực token thất bại: " + e.getMessage());
      return false;
    }

    return false;
  }

  private boolean isTokenFormatValid(String token) {
    try {
      String[] tokenParts = token.split("\\.");
      return tokenParts.length == 3;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean isSignatureValid(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private Key getSigningKey() {
    byte[] keyBytes = jwtConfig.getSecretKey().getBytes();
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private boolean isTokenExpired(String token) {
    final Date expiration = getClaimFromToken(token, Claims::getExpiration);
    return expiration.before(new Date());
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private boolean isIssuerToken(String token) {
    String tokenIssuer = getClaimFromToken(token, Claims::getIssuer);
    return tokenIssuer.equals(jwtConfig.getIssuer());
  }




}
