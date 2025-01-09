package nguyenduy.local.movie.controllers.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/admin")
public class MovieController1 {

  @GetMapping
  @PreAuthorize("@appAuthorizer.authorize(authentication, 'VIEW', this)")
  public String index () {
    return "hello";
  }
}
