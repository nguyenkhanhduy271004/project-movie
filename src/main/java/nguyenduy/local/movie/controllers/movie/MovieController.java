package nguyenduy.local.movie.controllers.movie;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import nguyenduy.local.movie.constant.SystemConstant;
import nguyenduy.local.movie.converter.MovieConverter;
import nguyenduy.local.movie.exceptions.MovieException;
import nguyenduy.local.movie.helper.CustomMessage;
import nguyenduy.local.movie.models.dtos.MovieDTO;
import nguyenduy.local.movie.models.entities.Movie;
import nguyenduy.local.movie.models.request.EpisodeRequest;
import nguyenduy.local.movie.models.request.MovieRequest;
import nguyenduy.local.movie.models.response.ApiResponse;
import nguyenduy.local.movie.resources.SuccessResource;
import nguyenduy.local.movie.services.interfaces.IMovieService;

import nguyenduy.local.movie.specifications.MovieSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/movie")
public class MovieController {

  @Autowired
  private IMovieService movieService;
  @Autowired
  private MovieConverter movieConverter;

  @GetMapping
  public ResponseEntity<?> getAllMovies(
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
    try {
      List<MovieDTO> movies = movieService.getAllMoveis(page, pageSize);
      return ResponseEntity.ok(ApiResponse.successWithData(movies, CustomMessage.getSuccess(SystemConstant.MOVIE), HttpStatus.OK));
    } catch (Exception e) {
      return handleException(e);
    }
  }


  @GetMapping("{id}")
  public ResponseEntity<?> getMovieById(@PathVariable("id") Long id) {
    try {
      MovieDTO movieDTO = movieService.findMovieById(id);
      return ResponseEntity.ok(ApiResponse.successWithData(movieDTO, CustomMessage.getWithIdSuccess(id, SystemConstant.MOVIE), HttpStatus.OK));
    } catch (MovieException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND,e.getMessage()));
    } catch (Exception e) {
      return handleException(e);
    }
  }


  @PostMapping()
//  @PreAuthorize("@appAuthorizer.authorize(authentication, 'CREATE', this)")
  public ResponseEntity<?> createMovie(@Valid @RequestBody MovieRequest movieRequest) {
    try {
      movieService.addMovie(movieRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.successNoData(CustomMessage.createSuccess(SystemConstant.MOVIE), HttpStatus.CREATED));
    } catch (Exception e) {
      return handleException(e);
    }
  }


  @PutMapping()
  @PreAuthorize("@appAuthorizer.authorize(authentication, 'UPDATE', this)")
  public ResponseEntity<?> updateMovie(@Valid @RequestBody MovieRequest movieRequest) {
    try {
      movieService.updateMovie(movieRequest);
      return ResponseEntity.ok(ApiResponse.successNoData(CustomMessage.updateSuccess(SystemConstant.MOVIE), HttpStatus.OK));
    } catch (MovieException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND, e.getMessage()));
    } catch (Exception e) {
      return handleException(e);
    }
  }


  @DeleteMapping
  @PreAuthorize("@appAuthorizer.authorize(authentication, 'DELETE', this)")
  public ResponseEntity<?> deleteMovie(@RequestParam(value = "id") Long id) {
    try {
      movieService.deleteMovie(id);
      return ResponseEntity.ok(ApiResponse.successNoData(CustomMessage.deleteSuccess(SystemConstant.MOVIE), HttpStatus.OK));
    } catch (MovieException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND,e.getMessage()));
    } catch (DataAccessException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ApiResponse.error(HttpStatus.BAD_REQUEST,CustomMessage.deleteFailed(SystemConstant.MOVIE)));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @GetMapping("/searchMovies")
  public ResponseEntity<?> searchMovies(
      @RequestParam(required = false) String type,
      @RequestParam(required = false) String category,
      @RequestParam(required = false) String lang,
      @RequestParam(defaultValue = "view") String sortBy) {

    List<Movie> movies = movieService.searchMovie(type, category, lang, sortBy);
    List<MovieDTO> result = new ArrayList<>();
    for (Movie movie:movies) {
      MovieDTO movieDTO = movieConverter.movieDtoConverter(movie);
      result.add(movieDTO);
    }

    return ResponseEntity.ok(ApiResponse.successWithData(result, CustomMessage.getSuccess(SystemConstant.MOVIE), HttpStatus.OK));
  }


  private ResponseEntity<?> handleException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR,SystemConstant.INTERNAL_SERVER_ERROR));
  }
}
