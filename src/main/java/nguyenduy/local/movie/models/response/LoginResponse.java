package nguyenduy.local.movie.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import nguyenduy.local.movie.models.dtos.UserDTO;

@Data
@AllArgsConstructor
public class LoginResponse {

  private final String token;
  private final String refreshToken;
  private final UserDTO userDTO;

}
