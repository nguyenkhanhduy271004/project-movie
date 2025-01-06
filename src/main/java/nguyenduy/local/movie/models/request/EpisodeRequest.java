package nguyenduy.local.movie.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EpisodeRequest {

  private Long movieId;

  private Long episodeId;

  @NotBlank(message = "Tên không được để trống")
  private String name;

  private String slug;

  @NotNull(message = "Số tập không được để trống")
  private Integer episodeNumber;

  @NotBlank(message = "Thời gian không được để trống")
  private String duration;

  @NotBlank(message = "URL video phim không được để trống")
  private String videoUrl;
}
