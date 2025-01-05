package nguyenduy.local.movie.controllers.user;

import nguyenduy.local.movie.models.dtos.UserDTO;
import nguyenduy.local.movie.models.entities.User;
import nguyenduy.local.movie.models.response.ApiResponse;
import nguyenduy.local.movie.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new RuntimeException("User không tồn tại"));

    UserDTO userDTO = UserDTO
        .builder()
        .id(user.getId())
        .phoneNumber(user.getPhoneNumber())
        .build();

    ApiResponse apiResponse = ApiResponse.success(userDTO);
    return ResponseEntity.ok(apiResponse);
  }
}
