package nguyenduy.local.movie.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtConfig {

  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.expiration}")
  private Long expiration;

  @Value("${jwt.refreshtoken_expiration}")
  private Long refreshTokenExpiration;

  @Value("${jwt.issuer}")
  private String issuer;
}
