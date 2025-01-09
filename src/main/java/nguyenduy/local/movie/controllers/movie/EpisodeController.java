package nguyenduy.local.movie.controllers.movie;

import jakarta.validation.Valid;
import nguyenduy.local.movie.constant.SystemConstant;
import nguyenduy.local.movie.helper.CustomMessage;
import nguyenduy.local.movie.models.dtos.EpisodeDTO;
import nguyenduy.local.movie.models.request.EpisodeRequest;
import nguyenduy.local.movie.models.response.ApiResponse;
import nguyenduy.local.movie.services.interfaces.IEpisodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/episode")
public class EpisodeController {

  @Autowired
  private IEpisodeService episodeService;

  @PostMapping
  @PreAuthorize("@appAuthorizer.authorize(authentication, 'CREATE', this)")
  public ResponseEntity<ApiResponse<Void>> createEpisode(@Valid @RequestBody EpisodeRequest episodeRequest) {
    try {
      episodeService.addEpisode(episodeRequest);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(ApiResponse.successNoData(CustomMessage.createSuccess(SystemConstant.EPISODE), HttpStatus.CREATED));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @GetMapping
  public ResponseEntity<?> getAllEpisodes() {
    try {
      List<EpisodeDTO> episodes = episodeService.getAllEpisodes();
      return ResponseEntity.ok(ApiResponse.successWithData(episodes, CustomMessage.getSuccess(SystemConstant.EPISODE), HttpStatus.OK));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getEpisodeById(@PathVariable Long id) {
    try {
      EpisodeDTO episode = episodeService.findEpisodeById(id);
      return ResponseEntity.ok(ApiResponse.successWithData(episode, CustomMessage.getWithIdSuccess(id, SystemConstant.EPISODE), HttpStatus.OK));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @PutMapping("/{id}")
  @PreAuthorize("@appAuthorizer.authorize(authentication, 'UPDATE', this)")
  public ResponseEntity<ApiResponse<Void>> updateEpisode(@PathVariable Long id, @Valid @RequestBody EpisodeRequest episodeRequest) {
    try {
      episodeRequest.setEpisodeId(id);
      episodeService.updateEpisode(episodeRequest);
      return ResponseEntity.ok(ApiResponse.successNoData(CustomMessage.updateSuccess(SystemConstant.EPISODE), HttpStatus.OK));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("@appAuthorizer.authorize(authentication, 'DELETE', this)")
  public ResponseEntity<ApiResponse<Void>> deleteEpisode(@PathVariable Long id) {
    try {
      episodeService.deleteEpisode(id);
      return ResponseEntity.ok(ApiResponse.successNoData(CustomMessage.deleteSuccess(SystemConstant.EPISODE), HttpStatus.OK));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  private ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, SystemConstant.INTERNAL_SERVER_ERROR));
  }
}
