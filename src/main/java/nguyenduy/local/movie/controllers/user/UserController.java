package nguyenduy.local.movie.controllers.user;

import nguyenduy.local.movie.models.entities.User;
import nguyenduy.local.movie.repositories.UserRepository;
import nguyenduy.local.movie.resources.SuccessResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @GetMapping
  public ResponseEntity<?> profile() {
    String phoneNumber = "0987654321";
    User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new RuntimeException("User không tồn tại"));
    SuccessResource<User> successResource = new SuccessResource<>("success", user);
    return ResponseEntity.ok(successResource);
  }
}
