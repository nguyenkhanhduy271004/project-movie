package nguyenduy.local.movie.models.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import nguyenduy.local.movie.models.entities.Episode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO {
  private Long id;
  private String name;
  private String slug;
  private String originName;
  private String content;
  private String type;
  private String thumbUrl;
  private String posterUrl;
  private String quality;
  private String lang;
  private Integer view;
  private String category;
  private String time;
  private List<ActorDTO> actors;
  private List<DirectorDTO> directors;
  private List<EpisodeDTO> episodes;
}
