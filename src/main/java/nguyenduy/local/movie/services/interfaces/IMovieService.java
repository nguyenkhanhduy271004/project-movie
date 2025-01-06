package nguyenduy.local.movie.services.interfaces;

import java.util.List;
import nguyenduy.local.movie.models.dtos.MovieDTO;
import nguyenduy.local.movie.models.entities.Movie;
import nguyenduy.local.movie.models.request.EpisodeRequest;
import nguyenduy.local.movie.models.request.MovieRequest;

public interface IMovieService {

  void addMovie(MovieRequest movieRequest);
  List<MovieDTO> getAllMoveis();
  MovieDTO findMovieById(Long id);
  void updateMovie(MovieRequest movieRequest);
  void deleteMovie(Long id);
}
