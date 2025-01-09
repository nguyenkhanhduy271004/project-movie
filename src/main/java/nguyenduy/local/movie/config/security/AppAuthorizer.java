package nguyenduy.local.movie.config.security;

import org.springframework.security.core.Authentication;

public interface AppAuthorizer {
  boolean authorize(Authentication authentication, String action, Object callerObj);
}
