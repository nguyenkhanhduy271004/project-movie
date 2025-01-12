package nguyenduy.local.movie.repositories;

import java.util.List;
import nguyenduy.local.movie.models.entities.Comment;
import nguyenduy.local.movie.models.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findCommentsByMovieId(Long movieId);
}
