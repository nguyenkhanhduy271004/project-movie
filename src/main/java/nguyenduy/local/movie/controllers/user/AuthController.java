package nguyenduy.local.movie.controllers.user;

import jakarta.validation.Valid;
import nguyenduy.local.movie.exceptions.UserAlreadyExistsException;
import nguyenduy.local.movie.models.entities.RefreshToken;
import nguyenduy.local.movie.models.request.LoginRequest;
import nguyenduy.local.movie.models.request.RegisterRequest;
import nguyenduy.local.movie.models.response.ApiResponse;
import nguyenduy.local.movie.models.response.LoginResponse;
import nguyenduy.local.movie.models.request.RefreshTokenRequest;
import nguyenduy.local.movie.models.response.MessageReponse;
import nguyenduy.local.movie.models.response.RefreshTokenResponse;
import nguyenduy.local.movie.models.response.TokenResponse;
import nguyenduy.local.movie.repositories.RefreshTokenRepository;
import nguyenduy.local.movie.resources.ErrorResource;
import nguyenduy.local.movie.services.JwtService;
import nguyenduy.local.movie.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@Validated
public class AuthController {

  @Autowired
  private IUserService userService;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @PostMapping("register")
  public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest registerRequest) {
    if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ApiResponse.error("Mật khẩu xác nhận không khớp"));
    }

    try {
      userService.register(registerRequest);
      return ResponseEntity.ok(ApiResponse.success("Đăng ký tài khoản thành công"));
    } catch (UserAlreadyExistsException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ApiResponse.error(e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ApiResponse.error("Đã xảy ra lỗi khi đăng ký tài khoản: " + e.getMessage()));
    }
  }




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

  @PostMapping("refresh")
  public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String bearerToken) {
    if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageReponse("Thiếu hoặc sai định dạng Authorization header"));
    }

    String refreshToken = bearerToken.substring(7);

    if (!jwtService.isRefreshTokenValid(refreshToken)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageReponse("Token không hợp lệ"));
    }

    RefreshToken existingToken = refreshTokenRepository.findByRefreshToken(refreshToken).get();
    if (existingToken == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageReponse("Token không hợp lệ hoặc đã hết hạn"));
    }

    Long userId = Long.valueOf(jwtService.getUserIdFromJwt(refreshToken));
    String username = jwtService.getUsernameFromJwt(refreshToken);

    String newToken = jwtService.generateToken(userId, username);
    String newRefreshToken = jwtService.generateRefreshToken(userId, username);
    ApiResponse apiResponse = ApiResponse.success(new RefreshTokenResponse(newToken, newRefreshToken));
    return ResponseEntity.ok(apiResponse);
  }


}
