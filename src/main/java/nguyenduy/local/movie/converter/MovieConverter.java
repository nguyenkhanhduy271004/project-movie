package nguyenduy.local.movie.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import nguyenduy.local.movie.models.dtos.ActorDTO;
import nguyenduy.local.movie.models.dtos.DirectorDTO;
import nguyenduy.local.movie.models.dtos.EpisodeDTO;
import nguyenduy.local.movie.models.dtos.MovieDTO;
import nguyenduy.local.movie.models.entities.Actor;
import nguyenduy.local.movie.models.entities.Director;
import nguyenduy.local.movie.models.entities.Episode;
import nguyenduy.local.movie.models.entities.Movie;
import nguyenduy.local.movie.models.request.EpisodeRequest;
import nguyenduy.local.movie.models.request.MovieRequest;
import nguyenduy.local.movie.repositories.ActorRepository;
import nguyenduy.local.movie.repositories.DirectorRepository;
import nguyenduy.local.movie.repositories.EpisodeRepository;
import nguyenduy.local.movie.repositories.MovieRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovieConverter {


  @Autowired
  private EpisodeRepository episodeRepository;

  @Autowired
  private ActorRepository actorRepository;

  @Autowired
  private DirectorRepository directorRepository;

  @Autowired
  private DirectorConverter directorConverter;

  @Autowired
  private ActorConverter actorConverter;

  public MovieDTO movieDtoConverter(Movie movie) {
    ModelMapper modelMapper = new ModelMapper();
    MovieDTO movieDTO = modelMapper.map(movie, MovieDTO.class);
    List<ActorDTO>  actors = new ArrayList<>();
    for (Actor actor : movie.getActors()) {
      ActorDTO actorDTO = actorConverter.actorDTOConverter(actor);
      actors.add(actorDTO);
    }
    movieDTO.setActors(actors);

    List<DirectorDTO>  directors = new ArrayList<>();
    for (Director director : movie.getDirectors()) {
      DirectorDTO directorDTO = directorConverter.directorDTOConverter(director);
      directors.add(directorDTO);
    }
    movieDTO.setDirectors(directors);

    List<EpisodeDTO>  episodes = new ArrayList<>();
    for (Episode episode : movie.getEpisodes()) {
      EpisodeDTO episodeDTO = this.episodeDTOConverter(episode);
      episodes.add(episodeDTO);
    }
    movieDTO.setEpisodes(episodes);
    return movieDTO;
  }

  public Movie movieConverter(MovieRequest movieRequest) {
    ModelMapper modelMapper = new ModelMapper();
    Movie movie = modelMapper.map(movieRequest, Movie.class);
    if (movieRequest.getActorIds() != null) {
      List<Actor> actors = actorRepository.findAllById(movieRequest.getActorIds());
      movie.setActors(actors);
    }

    if (movieRequest.getDirectorIds() != null) {
      List<Director> directors = directorRepository.findAllById(movieRequest.getDirectorIds());
      movie.setDirectors(directors);
    }

    return movie;
  }

  public Episode episodeConverter(EpisodeRequest episodeRequest) {
    Episode episode = new Episode();

    episode.setName(episodeRequest.getName());
    episode.setSlug(episodeRequest.getSlug());
    episode.setVideoUrl(episodeRequest.getVideoUrl());
    episode.setDuration(episodeRequest.getDuration());
    episode.setEpisodeNumber(episodeRequest.getEpisodeNumber());

    if (episodeRequest.getEpisodeId() != null) {
      episode.setId(episodeRequest.getEpisodeId());
    }

    return episode;
  }




  public EpisodeDTO episodeDTOConverter(Episode episode) {
    ModelMapper modelMapper = new ModelMapper();
    EpisodeDTO episodeDTO = modelMapper.map(episode, EpisodeDTO.class);
    return episodeDTO;
  }



}
