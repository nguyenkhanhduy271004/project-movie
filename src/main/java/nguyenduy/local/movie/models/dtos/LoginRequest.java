package nguyenduy.local.movie.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

  @NotBlank(message = "SĐT không được bỏ trống")
  private String phoneNumber;

  @NotBlank(message = "Mật khẩu không được bỏ trống")
  private String password;
}
