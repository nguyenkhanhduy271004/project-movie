package nguyenduy.local.movie.models.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class DirectorRequest {
  private Long id;

  @NotBlank(message = "Tên không được để trống")
  private String name;

  @NotBlank(message = "Giới tính không được đễ trống")
  private String gender;

  private String profilePictureUrl;

  @NotBlank(message = "Quốc tịch không được để trống")
  private String nationality;

  private List<String> awards;
}
