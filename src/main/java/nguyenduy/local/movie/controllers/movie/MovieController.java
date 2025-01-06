package nguyenduy.local.movie.controllers.movie;

import jakarta.validation.Valid;
import java.util.List;

import nguyenduy.local.movie.exceptions.MovieException;
import nguyenduy.local.movie.models.dtos.MovieDTO;
import nguyenduy.local.movie.models.request.EpisodeRequest;
import nguyenduy.local.movie.models.request.MovieRequest;
import nguyenduy.local.movie.models.response.ApiResponse;
import nguyenduy.local.movie.resources.SuccessResource;
import nguyenduy.local.movie.services.interfaces.IMovieService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/movie")
public class MovieController {

  @Autowired
  private IMovieService movieService;

  @GetMapping
  public ResponseEntity<?> getAllMovies() {
    try {
      List<MovieDTO> movies = movieService.getAllMoveis();
      return ResponseEntity.ok(ApiResponse.successWithData(movies, "Lấy dữ liệu phim thành công!", HttpStatus.OK));
    } catch (Exception e) {
      return handleException(e);
    }
  }


  @GetMapping("{id}")
  public ResponseEntity<?> getMovieById(@PathVariable("id") Long id) {
    try {
      MovieDTO movieDTO = movieService.findMovieById(id);
      return ResponseEntity.ok(ApiResponse.successWithData(movieDTO, "Lấy dữ liệu phim với id: " + id + " thành công!", HttpStatus.OK));
    } catch (MovieException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND,e.getMessage()));
    } catch (Exception e) {
      return handleException(e);
    }
  }


  @PostMapping()
  public ResponseEntity<?> createMovie(@Valid @RequestBody MovieRequest movieRequest) {
    try {
      movieService.addMovie(movieRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.successNoData("Thêm phim thành công", HttpStatus.CREATED));
    } catch (Exception e) {
      return handleException(e);
    }
  }


  @PutMapping()
  public ResponseEntity<?> updateMovie(@Valid @RequestBody MovieRequest movieRequest) {
    try {
      movieService.updateMovie(movieRequest);
      return ResponseEntity.ok(ApiResponse.successNoData("Cập nhật phim thành công", HttpStatus.OK));
    } catch (MovieException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND, e.getMessage()));
    } catch (Exception e) {
      return handleException(e);
    }
  }


  @DeleteMapping
  public ResponseEntity<?> deleteMovie(@RequestParam(value = "id") Long id) {
    try {
      movieService.deleteMovie(id);
      return ResponseEntity.ok(ApiResponse.successNoData("Xóa phim thành công", HttpStatus.OK));
    } catch (MovieException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND,e.getMessage()));
    } catch (DataAccessException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ApiResponse.error(HttpStatus.BAD_REQUEST,"Xóa phim thất bại!"));
    } catch (Exception e) {
      return handleException(e);
    }
  }


  private ResponseEntity<?> handleException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR,"Lỗi hệ thống: " + e.getMessage()));
  }
}
