package nguyenduy.local.movie.controllers.movie;

import jakarta.validation.Valid;
import nguyenduy.local.movie.models.dtos.EpisodeDTO;
import nguyenduy.local.movie.models.request.EpisodeRequest;
import nguyenduy.local.movie.models.response.ApiResponse;
import nguyenduy.local.movie.services.interfaces.IEpisodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/episode")
public class EpisodeController {

  @Autowired
  private IEpisodeService episodeService;


  @PostMapping
  public ResponseEntity<ApiResponse<Void>> createEpisode(@Valid @RequestBody EpisodeRequest episodeRequest) {
    try {
      episodeService.addEpisode(episodeRequest);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(ApiResponse.successNoData("Thêm tập thành công", HttpStatus.CREATED));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
  }


  @GetMapping
  public ResponseEntity<ApiResponse<List<EpisodeDTO>>> getAllEpisodes() {
    try {
      List<EpisodeDTO> episodes = episodeService.getAllEpisodes();
      return ResponseEntity.ok(ApiResponse.successWithData(episodes, "Lấy danh sách tập phim thành công", HttpStatus.OK));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
  }


  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<EpisodeDTO>> getEpisodeById(@PathVariable Long id) {
    try {
      EpisodeDTO episode = episodeService.findEpisodeById(id);
      return ResponseEntity.ok(ApiResponse.successWithData(episode, "Lấy tập phim thành công", HttpStatus.OK));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
  }


  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> updateEpisode(@PathVariable Long id, @Valid @RequestBody EpisodeRequest episodeRequest) {
    try {
      episodeRequest.setEpisodeId(id);
      episodeService.updateEpisode(episodeRequest);
      return ResponseEntity.ok(ApiResponse.successNoData("Cập nhật tập phim thành công", HttpStatus.OK));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteEpisode(@PathVariable Long id) {
    try {
      episodeService.deleteEpisode(id);
      return ResponseEntity.ok(ApiResponse.successNoData("Xóa tập phim thành công", HttpStatus.OK));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
  }
}
