package nguyenduy.local.movie.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

  private boolean success;
  private String message;
  private T data;

  public static <T> ApiResponse<T> empty() {
    return success(null);
  }

  public static <T> ApiResponse<T> success(String customMessage) {
    return ApiResponse.<T>builder()
        .message(customMessage)
        .success(true)
        .build();
  }

  public static <T> ApiResponse<T> success() {
    return ApiResponse.<T>builder()
        .message("SUCCESS!")
        .success(true)
        .build();
  }

  public static <T> ApiResponse<T> success(T data) {
    return ApiResponse.<T>builder()
        .message("SUCCESS!")
        .data(data)
        .success(true)
        .build();
  }

  public static <T> ApiResponse<T> error() {
    return ApiResponse.<T>builder()
        .message("ERROR!")
        .success(false)
        .build();
  }

  public static <T> ApiResponse<T> error(String customMessage) {
    return ApiResponse.<T>builder()
        .message(customMessage)
        .success(false)
        .build();
  }
}
