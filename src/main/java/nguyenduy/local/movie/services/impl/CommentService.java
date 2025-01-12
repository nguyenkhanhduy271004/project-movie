package nguyenduy.local.movie.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import nguyenduy.local.movie.converter.CommentConverter;
import nguyenduy.local.movie.models.dtos.CommentDTO;
import nguyenduy.local.movie.models.entities.Comment;
import nguyenduy.local.movie.models.entities.Movie;
import nguyenduy.local.movie.models.request.CommentRequest;
import nguyenduy.local.movie.repositories.CommentRepository;
import nguyenduy.local.movie.services.JwtService;
import nguyenduy.local.movie.services.interfaces.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CommentService implements ICommentService {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private CommentConverter commentConverter;

  @Autowired
  private CommentRepository commentRepository;

  @Override
  public void createAndEdit(HttpServletRequest request, CommentRequest commentRequest) {
    try {
      String authorizationHeader = request.getHeader("Authorization");

      if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
        throw new IllegalArgumentException("Authorization header is missing or invalid.");
      }

      String token = authorizationHeader.substring(7);

      Long userId = Long.parseLong(jwtService.getUserIdFromJwt(token));

      Comment comment = commentConverter.commentConvert(userId, commentRequest);

      commentRepository.save(comment);

    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid user ID format in JWT token.", e);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Error processing request: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("An unexpected error occurred while creating or editing the comment.", e);
    }
  }

  @Override
  public void delete(Long id) {
    try {
      commentRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new IllegalArgumentException("Không tìm thấy comment với id: " + id, e);
    } catch (DataAccessException e) {
      throw new RuntimeException("Lỗi khi truy cập cơ sở dữ liệu trong quá trình xóa comment.", e);
    } catch (Exception e) {
      throw new RuntimeException("Đã xảy ra lỗi không mong muốn khi xóa comment.", e);
    }
  }

  @Override
  public List<CommentDTO> getCommentsByMovieId(Long movieId) {
    List<Comment> comments = commentRepository.findCommentsByMovieId(movieId);
    List<CommentDTO> result = new ArrayList<>();
    for(Comment comment:comments) {
      CommentDTO commentDTO = commentConverter.commentDTOConverter(comment);
      result.add(commentDTO);
    }

    return result;
  }


}
