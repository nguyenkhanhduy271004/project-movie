package nguyenduy.local.movie.converter;

import java.util.List;
import java.util.stream.Collectors;
import nguyenduy.local.movie.models.dtos.MovieDTO;
import nguyenduy.local.movie.models.entities.Actor;
import nguyenduy.local.movie.models.entities.Director;
import nguyenduy.local.movie.models.entities.Episode;
import nguyenduy.local.movie.models.entities.Movie;
import nguyenduy.local.movie.models.request.MovieRequest;
import nguyenduy.local.movie.repositories.ActorRepository;
import nguyenduy.local.movie.repositories.DirectorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovieConverter {

  @Autowired
  private ActorRepository actorRepository;

  @Autowired
  private DirectorRepository directorRepository;

  public MovieDTO movieDtoConverter(Movie movie) {
    return MovieDTO.builder()
        .id(movie.getId())
        .name(movie.getName())
        .slug(movie.getSlug())
        .originName(movie.getOriginName())
        .content(movie.getContent())
        .quality(movie.getQuality())
        .lang(movie.getLang())
        .thumbUrl(movie.getThumbUrl())
        .posterUrl(movie.getPosterUrl())
        .view(movie.getView())
        .actors(movie.getActors().stream()
            .map(Actor::getName)
            .collect(Collectors.toList()))
        .directors(movie.getDirectors().stream()
            .map(Director::getName)
            .collect(Collectors.toList()))
        .episodes(movie.getEpisodes().stream()
            .map(Episode::getVideoUrl)
            .collect(Collectors.toList()))
        .build();
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


}
