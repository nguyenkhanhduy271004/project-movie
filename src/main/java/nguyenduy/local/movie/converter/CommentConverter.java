package nguyenduy.local.movie.converter;

import nguyenduy.local.movie.models.dtos.CommentDTO;
import nguyenduy.local.movie.models.dtos.MovieDTO;
import nguyenduy.local.movie.models.entities.Comment;
import nguyenduy.local.movie.models.entities.Movie;
import nguyenduy.local.movie.models.entities.User;
import nguyenduy.local.movie.models.request.CommentRequest;
import nguyenduy.local.movie.repositories.MovieRepository;
import nguyenduy.local.movie.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MovieRepository movieRepository;

  public Comment commentConvert(Long userId, CommentRequest commentRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

    Movie movie = movieRepository.findById(commentRequest.getMovieId())
        .orElseThrow(() -> new IllegalArgumentException("Movie not found with ID: " + commentRequest.getMovieId()));

    Comment comment;

    if (commentRequest.getCommentId() != null) {
      comment = new Comment();
      comment.setId(commentRequest.getCommentId());
      comment.setUser(user);
      comment.setMovie(movie);
      comment.setContent(commentRequest.getContent());
    } else {
      comment = Comment.builder()
          .user(user)
          .movie(movie)
          .content(commentRequest.getContent())
          .build();
    }

    return comment;
  }

  public CommentDTO commentDTOConverter(Comment comment) {
    CommentDTO commentDTO = CommentDTO.builder()
        .username(comment.getUser().getPhoneNumber())
        .content(comment.getContent())
        .build();
    return commentDTO;
  }
}
