package nguyenduy.local.movie.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {

  @NotBlank(message = "RefreshToken không được để trống")
  private String refreshToken;
}
