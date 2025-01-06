package nguyenduy.local.movie.controllers.movie;

import jakarta.validation.Valid;
import java.util.List;
import nguyenduy.local.movie.exceptions.ActorException;
import nguyenduy.local.movie.models.dtos.ActorDTO;
import nguyenduy.local.movie.models.request.ActorRequest;
import nguyenduy.local.movie.models.response.ApiResponse;
import nguyenduy.local.movie.services.interfaces.IActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/actor")
public class ActorController {

  @Autowired
  private IActorService actorService;

  @GetMapping
  public ResponseEntity<?> getAllActors() {
    try {
      List<ActorDTO> actors = actorService.findAll();
      return ResponseEntity.ok(
          ApiResponse.successWithData(actors, "Lấy dữ liệu diễn tất cả diễn viên thành công!", HttpStatus.OK));
    } catch (Exception e) {
      return handleException(e);
    }
  }


  @GetMapping("{id}")
  public ResponseEntity<?> getActorById(@PathVariable("id") Long id) {
    try {
      ActorDTO actorDTO = actorService.findById(id);
      return ResponseEntity.ok(ApiResponse.successWithData(actorDTO, "Lấy dữ liệu diễn viên với id: " + id + " thành công!", HttpStatus.OK));
    } catch (ActorException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND,e.getMessage()));
    } catch (Exception e) {
      return handleException(e);
    }
  }


  @PostMapping()
  public ResponseEntity<?> createActor(@Valid @RequestBody ActorRequest actorRequest) {
    try {
      actorService.create(actorRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.successNoData("Thêm diễn viên thành công", HttpStatus.CREATED));
    } catch (Exception e) {
      return handleException(e);
    }
  }


  @PutMapping()
  public ResponseEntity<?> updateActor(@Valid @RequestBody ActorRequest actorRequest) {
    try {
      actorService.update(actorRequest);
      return ResponseEntity.ok(ApiResponse.successNoData("Cập nhật thông tin diễn viên thành công", HttpStatus.OK));
    } catch (ActorException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND, e.getMessage()));
    } catch (Exception e) {
      return handleException(e);
    }
  }


  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteActor(@PathVariable Long id) {
    try {
      actorService.delete(id);
      return ResponseEntity.ok(ApiResponse.successNoData("Xóa diễn viên thành công", HttpStatus.OK));
    } catch (ActorException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND,e.getMessage()));
    } catch (DataAccessException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ApiResponse.error(HttpStatus.BAD_REQUEST,"Xóa diễn viên thất bại!"));
    } catch (Exception e) {
      return handleException(e);
    }
  }


  private ResponseEntity<?> handleException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR,"Lỗi hệ thống: " + e.getMessage()));
  }
}
