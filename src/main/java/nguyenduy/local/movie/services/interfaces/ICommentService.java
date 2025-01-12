package nguyenduy.local.movie.services.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import nguyenduy.local.movie.models.dtos.CommentDTO;
import nguyenduy.local.movie.models.request.CommentRequest;

public interface ICommentService {

  void createAndEdit(HttpServletRequest request, CommentRequest commentRequest);
  void delete(Long id);
  List<CommentDTO> getCommentsByMovieId(Long movieId);
}
