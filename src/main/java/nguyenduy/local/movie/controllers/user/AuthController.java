package nguyenduy.local.movie.controllers.user;

import jakarta.validation.Valid;
import nguyenduy.local.movie.models.dtos.LoginRequest;
import nguyenduy.local.movie.models.dtos.LoginResponse;
import nguyenduy.local.movie.models.dtos.UserDTO;
import nguyenduy.local.movie.resources.ErrorResource;
import nguyenduy.local.movie.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@Validated
public class AuthController {

  @Autowired
  private IUserService userService;

  @PostMapping("login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
    Object result = userService.authenticate(loginRequest);
    if(result instanceof LoginResponse loginResponse) {
      return ResponseEntity.ok(loginResponse);
    }
    if (result instanceof ErrorResource errorResource) {
      return ResponseEntity.unprocessableEntity().body(errorResource);
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Network error");
  }
}
