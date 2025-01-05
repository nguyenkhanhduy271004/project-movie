package nguyenduy.local.movie.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpisodeDTO {
  private String name;
  private Integer episodeNumber;
  private String videoUrl;
}

