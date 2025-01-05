package nguyenduy.local.movie.models.response;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TokenResponse {
  private final String token;
}
