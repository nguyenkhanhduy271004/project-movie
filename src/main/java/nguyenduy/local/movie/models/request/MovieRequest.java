package nguyenduy.local.movie.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class MovieRequest {

  private Long id;

  @NotBlank(message = "Tên phim không được để trống")
  private String name;

  @NotBlank(message = "Slug không được để trống")
  private String slug;

  @NotBlank(message = "Tên gốc không được để trống")
  private String originName;

  private String content;

  @NotBlank(message = "Loại phim không được để trống")
  private String type;

  private String thumbUrl;
  private String posterUrl;

  @NotNull(message = "Thông tin bản quyền không được để trống")
  private Boolean isCopyright;

  private Boolean hasSub;
  private String time;
  private String episodeCurrent;
  private String episodeTotal;
  private String quality;
  private String lang;
  private Integer view;
  private String category;

  private List<Long> actorIds;
  private List<Long> directorIds;
}
