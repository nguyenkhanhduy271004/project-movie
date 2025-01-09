package nguyenduy.local.movie.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nguyenduy.local.movie.converter.MovieConverter;
import nguyenduy.local.movie.exceptions.MovieException;
import nguyenduy.local.movie.models.dtos.MovieDTO;
import nguyenduy.local.movie.models.entities.Movie;
import nguyenduy.local.movie.models.request.MovieRequest;
import nguyenduy.local.movie.repositories.EpisodeRepository;
import nguyenduy.local.movie.repositories.MovieRepository;
import nguyenduy.local.movie.services.interfaces.IMovieService;

import nguyenduy.local.movie.specifications.MovieSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements IMovieService {

  @Autowired
  private MovieRepository movieRepository;
  @Autowired
  private MovieConverter movieConverter;
  @Autowired
  private EpisodeRepository episodeRepository;

  @Override
  public void addMovie(MovieRequest movieRequest) {
    try {
      Movie movie = movieConverter.movieConverter(movieRequest);
      movieRepository.save(movie);
    } catch (DataAccessException e) {
      throw new MovieException("Lỗi khi lưu phim vào database: " + e.getMessage());
    } catch (Exception e) {
      throw new MovieException("Thêm phim không thành công: " + e.getMessage());
    }
  }

  @Override
  public List<MovieDTO> getAllMoveis(int page, int pageSize) {
    try {
      Pageable paging = PageRequest.of(page, pageSize);
      Page<Movie> pagedResult = movieRepository.findAll(paging);
      List<MovieDTO> movieDTOs = new ArrayList<>();
      for (Movie movie : pagedResult.getContent()) {
        MovieDTO movieDTO = movieConverter.movieDtoConverter(movie);
        movieDTOs.add(movieDTO);
      }
      return movieDTOs;
    } catch (Exception e) {
      throw new MovieException("Không thể lấy danh sách phim: " + e.getMessage());
    }
  }

  @Override
  public MovieDTO findMovieById(Long id) {
    try {
      Optional<Movie> movieOptional = movieRepository.findById(id);
      if (movieOptional.isPresent()) {
        return movieConverter.movieDtoConverter(movieOptional.get());
      } else {
        throw new MovieException("Không tìm thấy phim với ID: " + id);
      }
    } catch (Exception e) {
      throw new MovieException("Lỗi khi tìm kiếm phim: " + e.getMessage());
    }
  }

  @Override
  public void updateMovie(MovieRequest movieRequest) {
    try {
      Movie movie = movieConverter.movieConverter(movieRequest);
      if (movieRepository.existsById(movie.getId())) {
        movieRepository.save(movie);
      } else {
        throw new MovieException("Không thể cập nhật, phim không tồn tại với ID: " + movie.getId());
      }
    } catch (Exception e) {
      throw new MovieException("Lỗi khi cập nhật phim: " + e.getMessage());
    }
  }

  @Override
  public void deleteMovie(Long id) {
    try {
      if (movieRepository.existsById(id)) {
        movieRepository.deleteById(id);
      } else {
        throw new MovieException("Không thể xóa, phim không tồn tại với ID: " + id);
      }
    } catch (DataAccessException e) {
      throw new MovieException("Lỗi khi xóa phim khỏi database: " + e.getMessage());
    } catch (Exception e) {
      throw new MovieException("Lỗi khi xóa phim: " + e.getMessage());
    }
  }

  @Override
  public List<Movie> searchMovie(String type, String category, String lang, String sortBy) {
    Specification<Movie> spec = MovieSpecifications.findMovie(type, category, lang);
    return movieRepository.findAll(spec);
  }



}
