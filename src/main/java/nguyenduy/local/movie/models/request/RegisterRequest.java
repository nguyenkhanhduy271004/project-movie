package nguyenduy.local.movie.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

  @NotBlank(message = "Username không được bỏ trống ")
  private String phoneNumber;

  @NotBlank(message = "Password không được bỏ trống")
  private String password;

  @NotBlank(message = "Confirm passowrd không được bỏ trống")
  private String confirmPassword;
}
