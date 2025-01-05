package nguyenduy.local.movie.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;
import nguyenduy.local.movie.config.JwtConfig;
import nguyenduy.local.movie.helper.JwtAuthFilter;
import nguyenduy.local.movie.models.entities.RefreshToken;
import nguyenduy.local.movie.models.entities.User;
import nguyenduy.local.movie.repositories.RefreshTokenRepository;
import nguyenduy.local.movie.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final JwtConfig jwtConfig;
  private final Key key;
  private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;


  public JwtService(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
    this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(jwtConfig.getSecretKey().getBytes()));
  }

  public String generateToken(Long userId, String phoneNumber) {
    Date now = new Date();
    long expirationMillis = jwtConfig.getExpiration();


    Date expiredDate = new Date(now.getTime() + expirationMillis);

    return Jwts.builder()
        .setSubject(String.valueOf(userId))
        .claim("username", phoneNumber)
        .setIssuer(jwtConfig.getIssuer())
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();
  }

  public String generateRefreshToken(Long userId, String phoneNumber) {
    Date now = new Date();
    long expirationMillis = jwtConfig.getRefreshTokenExpiration();

    Date expiredDate = new Date(now.getTime() + expirationMillis);

    String refreshTokenString = Jwts.builder()
        .setSubject(String.valueOf(userId))
        .claim("username", phoneNumber)
        .setIssuer(jwtConfig.getIssuer())
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();

    LocalDateTime localExpiredDate = expiredDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    RefreshToken refreshTokenExist = refreshTokenRepository.findByUserId(userId);
    if (refreshTokenExist != null) {
      refreshTokenExist.setRefreshToken(refreshTokenString);
      refreshTokenExist.setExpiryDate(localExpiredDate);
      refreshTokenRepository.save(refreshTokenExist);
    } else {
      User user = userRepository.findById(userId).get();
      RefreshToken refreshToken = new RefreshToken(refreshTokenString, user, localExpiredDate);
      refreshTokenRepository.save(refreshToken);
    }

    return refreshTokenString;
  }



  public String getUsernameFromJwt(String token) {
    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    return claims.get("username", String.class);
  }
  

  public String getUserIdFromJwt(String token) {
    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    return claims.getSubject();
  }


  public boolean isTokenFormatValid(String token) {
    try {
      String[] tokenParts = token.split("\\.");
      return tokenParts.length == 3;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean isSignatureValid(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
      return true;
    } catch (SignatureException e) {
      return false;
    }
  }

  private Key getSigningKey() {
    byte[] keyBytes = jwtConfig.getSecretKey().getBytes();
    return Keys.hmacShaKeyFor(Base64.getEncoder().encode(keyBytes));
  }

  public boolean isTokenExpired(String token) {
    try {
      Claims claims = getAllClaimsFromToken(token);
      return claims.getExpiration().before(new Date());
    } catch (Exception e) {
      return true;
    }
  }


  public <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
   try {
     return Jwts.parserBuilder()
         .setSigningKey(key)
         .build()
         .parseClaimsJws(token)
         .getBody();
   } catch (ExpiredJwtException e) {
     return null;
   }
  }

  public boolean isIssuerToken(String token) {
    String tokenIssuer = getClaimFromToken(token, Claims::getIssuer);
    return tokenIssuer.equals(jwtConfig.getIssuer());
  }

  public boolean isRefreshTokenValid(String token) {

    try {
      Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
      RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(token).orElseThrow(() -> new RuntimeException("Refresh token không tồn tại"));

      final Date expiration = getClaimFromToken(token, Claims::getExpiration);

      return expiration.after(new Date());
    } catch (Exception e) {
      return false;
    }
  }




}
