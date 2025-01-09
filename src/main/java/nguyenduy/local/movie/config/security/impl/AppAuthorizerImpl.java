package nguyenduy.local.movie.config.security.impl;

import nguyenduy.local.movie.config.security.AppAuthorizer;
import nguyenduy.local.movie.services.interfaces.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.util.*;

@Service("appAuthorizer")
public class AppAuthorizerImpl implements AppAuthorizer {


  private final Logger logger = LoggerFactory.getLogger(AppAuthorizerImpl.class);

  @Override
  public boolean authorize(Authentication authentication, String action, Object callerObj) {
    String securedPath = extractSecuredPath(callerObj);
    if (securedPath == null || "".equals(securedPath.trim())) {
      return true;
    }
    boolean isAllow = false;
    try {
      if (authentication == null) {
        return false;
      }

      Object principal = authentication.getPrincipal();
      if (principal == null) {
        return false;
      }

      if (principal instanceof UserDetails) {
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();

        if (username != null && !username.trim().isEmpty()) {
          if (userDetails.getAuthorities().stream()
              .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            isAllow = true;
          }
        }
      }

    } catch (Exception e) {
      logger.error("Authorization error: ", e);
      throw e;
    }
    return isAllow;
  }

  private String extractSecuredPath(Object callerObj) {
    Class<?> clazz = ResolvableType.forClass(callerObj.getClass()).getRawClass();
    Optional<Annotation> annotation = Arrays.stream(clazz.getAnnotations())
        .filter(ann -> ann instanceof RequestMapping)
        .findFirst();

    logger.debug("Found caller class: {}", ResolvableType.forClass(callerObj.getClass()).getType().getTypeName());

    if (annotation.isPresent()) {
      return ((RequestMapping) annotation.get()).value()[0];
    }
    return null;
  }
}
