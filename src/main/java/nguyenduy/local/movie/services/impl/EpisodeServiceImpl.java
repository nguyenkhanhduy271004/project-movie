package nguyenduy.local.movie.services.impl;

import java.util.ArrayList;
import java.util.List;
import nguyenduy.local.movie.converter.EpisodeConverter;
import nguyenduy.local.movie.converter.MovieConverter;
import nguyenduy.local.movie.exceptions.EpisodeException;
import nguyenduy.local.movie.exceptions.MovieException;
import nguyenduy.local.movie.models.dtos.EpisodeDTO;
import nguyenduy.local.movie.models.dtos.MovieDTO;
import nguyenduy.local.movie.models.entities.Episode;
import nguyenduy.local.movie.models.entities.Movie;
import nguyenduy.local.movie.models.request.EpisodeRequest;
import nguyenduy.local.movie.repositories.EpisodeRepository;
import nguyenduy.local.movie.repositories.MovieRepository;
import nguyenduy.local.movie.services.interfaces.IEpisodeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class EpisodeServiceImpl implements IEpisodeService {

  @Autowired
  private MovieRepository movieRepository;

  @Autowired
  private MovieConverter movieConverter;

  @Autowired
  private EpisodeRepository episodeRepository;


  @Override
  public void addEpisode(EpisodeRequest episodeRequest) {
    try {
      Movie movie = movieRepository.findById(episodeRequest.getMovieId())
          .orElseThrow(() -> new RuntimeException("Movie not found"));

      Episode episode = movieConverter.episodeConverter(episodeRequest);

      if (movie.getEpisodes() == null) {
        movie.setEpisodes(new ArrayList<>());
      }

      movie.getEpisodes().add(episode);

      episode.setMovie(movie);

      movieRepository.save(movie);


    } catch (Exception e) {
      throw new MovieException("Lỗi khi thêm tập phim: " + e.getMessage());
    }
  }

  @Override
  public List<EpisodeDTO> getAllEpisodes() {
    return List.of();
  }

  @Override
  public EpisodeDTO findEpisodeById(Long id) {
    try {
      Episode episode = episodeRepository.findById(id).orElseThrow(() -> new EpisodeException("Không tìm thấy tập phim id: " + id));
      return movieConverter.episodeDTOConverter(episode);
    } catch (Exception e) {
      throw new EpisodeException("Network error: " + e.getMessage());
    }
  }

  @Override
  public void updateEpisode(EpisodeRequest episodeRequest) {
    try {
      Episode episode = episodeRepository.findById(episodeRequest.getEpisodeId())
          .orElseThrow(() -> new EpisodeException("Không tìm thấy tập phim id: " + episodeRequest.getEpisodeId()));
      episode.setName(episodeRequest.getName());
      episode.setSlug(episodeRequest.getSlug());
      episode.setVideoUrl(episodeRequest.getVideoUrl());
      episode.setDuration(episodeRequest.getDuration());
      episode.setEpisodeNumber(episodeRequest.getEpisodeNumber());
      episodeRepository.save(episode);
    } catch (Exception e) {
      throw new EpisodeException("Network error: " + e.getMessage());
    }
  }


  @Override
  public void deleteEpisode(Long id) {
    try {
      episodeRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new EpisodeException("Không tìm thấy tập phim id: " + id);
    } catch (Exception e) {
      throw new EpisodeException("Lỗi khi xóa tập phim: " + e.getMessage());
    }
  }


}
