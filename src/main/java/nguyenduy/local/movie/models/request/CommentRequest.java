package nguyenduy.local.movie.models.request;

import lombok.Data;

@Data
public class CommentRequest {

  private Long commentId;
  private Long movieId;
  private String content;

}
