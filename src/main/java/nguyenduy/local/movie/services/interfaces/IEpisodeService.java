package nguyenduy.local.movie.services.interfaces;

import java.util.List;
import nguyenduy.local.movie.models.dtos.EpisodeDTO;
import nguyenduy.local.movie.models.dtos.MovieDTO;
import nguyenduy.local.movie.models.entities.Episode;
import nguyenduy.local.movie.models.request.EpisodeRequest;
import nguyenduy.local.movie.models.request.MovieRequest;

public interface IEpisodeService {

  void addEpisode(EpisodeRequest episodeRequest);
  List<EpisodeDTO> getAllEpisodes();
  EpisodeDTO findEpisodeById(Long id);
  void updateEpisode(EpisodeRequest episodeRequest);
  void deleteEpisode(Long id);

}
