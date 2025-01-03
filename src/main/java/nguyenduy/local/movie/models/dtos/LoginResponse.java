package nguyenduy.local.movie.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class LoginResponse {

  private final String token;
  private final UserDTO userDTO;

}
