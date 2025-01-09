package nguyenduy.local.movie.controllers.movie;

import jakarta.validation.Valid;
import java.util.List;
import nguyenduy.local.movie.constant.SystemConstant;
import nguyenduy.local.movie.exceptions.ActorException;
import nguyenduy.local.movie.helper.CustomMessage;
import nguyenduy.local.movie.models.dtos.ActorDTO;
import nguyenduy.local.movie.models.request.ActorRequest;
import nguyenduy.local.movie.models.response.ApiResponse;
import nguyenduy.local.movie.services.interfaces.IActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/actor")
public class ActorController {

  @Autowired
  private IActorService actorService;

  @GetMapping
  public ResponseEntity<?> getAllActors() {
    try {
      List<ActorDTO> actors = actorService.findAll();
      return ResponseEntity.ok(ApiResponse.successWithData(actors, CustomMessage.getSuccess(SystemConstant.ACTOR), HttpStatus.OK));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @GetMapping("{id}")
  public ResponseEntity<?> getActorById(@PathVariable("id") Long id) {
    try {
      ActorDTO actorDTO = actorService.findById(id);
      return ResponseEntity.ok(ApiResponse.successWithData(actorDTO, CustomMessage.getWithIdSuccess(id, SystemConstant.ACTOR), HttpStatus.OK));
    } catch (ActorException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(HttpStatus.NOT_FOUND, e.getMessage()));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> createActor(@Valid @RequestBody ActorRequest actorRequest) {
    try {
      actorService.create(actorRequest);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(ApiResponse.successNoData(CustomMessage.createSuccess(SystemConstant.ACTOR), HttpStatus.CREATED));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @PutMapping
  public ResponseEntity<ApiResponse<Void>> updateActor(@Valid @RequestBody ActorRequest actorRequest) {
    try {
      actorService.update(actorRequest);
      return ResponseEntity.ok(ApiResponse.successNoData(CustomMessage.updateSuccess(SystemConstant.ACTOR), HttpStatus.OK));
    } catch (ActorException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(HttpStatus.NOT_FOUND, e.getMessage()));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteActor(@PathVariable Long id) {
    try {
      actorService.delete(id);
      return ResponseEntity.ok(ApiResponse.successNoData(CustomMessage.deleteSuccess(SystemConstant.ACTOR), HttpStatus.OK));
    } catch (ActorException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(HttpStatus.NOT_FOUND, e.getMessage()));
    } catch (DataAccessException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ApiResponse.error(HttpStatus.BAD_REQUEST, CustomMessage.deleteFailed(SystemConstant.ACTOR)));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  private ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, SystemConstant.INTERNAL_SERVER_ERROR));
  }
}
