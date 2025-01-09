package nguyenduy.local.movie.repositories;

import java.util.List;
import nguyenduy.local.movie.models.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>,
    JpaSpecificationExecutor<Movie> {

//  @Query("SELECT m FROM Movie m WHERE " +
//      "(m.type = :type OR :type = '') AND " +
//      "(m.category = :category OR :category = '') AND " +
//      "(m.lang = :lang OR :lang = '') " +
//      "ORDER BY CASE WHEN :sortBy = 'view' THEN m.view END DESC")
//  List<Movie> searchMovie(String type, String category, String lang, String sortBy);

}
