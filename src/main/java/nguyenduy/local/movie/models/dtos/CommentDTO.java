package nguyenduy.local.movie.models.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDTO {

  private String username;
  private String content;

}
