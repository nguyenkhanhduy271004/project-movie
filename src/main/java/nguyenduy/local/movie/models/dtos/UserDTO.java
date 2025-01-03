package nguyenduy.local.movie.models.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class UserDTO {

  private final Long id;
  private final String phoneNumber;
}
