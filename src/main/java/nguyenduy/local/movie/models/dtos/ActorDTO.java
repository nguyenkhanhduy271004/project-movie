package nguyenduy.local.movie.models.dtos;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ActorDTO {

  private String name;

  private String gender;

  private String profilePictureUrl;

  private String nationality;

  private String awards;
}
