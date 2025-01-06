package nguyenduy.local.movie.controllers.movie;

import jakarta.validation.Valid;
import java.util.List;
import nguyenduy.local.movie.exceptions.DirectorException;
import nguyenduy.local.movie.models.dtos.DirectorDTO;
import nguyenduy.local.movie.models.request.DirectorRequest;
import nguyenduy.local.movie.models.response.ApiResponse;
import nguyenduy.local.movie.services.interfaces.IDirectorService;
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
@RequestMapping("api/v1/director")
public class DirectorController {

  @Autowired
  private IDirectorService directorService;

  @GetMapping
  public ResponseEntity<?> getAllDirectors() {
    try {
      List<DirectorDTO> directors = directorService.findAll();
      return ResponseEntity.ok(
          ApiResponse.successWithData(directors, "Lấy dữ liệu tất cả đạo diễn thành công!", HttpStatus.OK));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @GetMapping("{id}")
  public ResponseEntity<?> getDirectorById(@PathVariable("id") Long id) {
    try {
      DirectorDTO directorDTO = directorService.findById(id);
      return ResponseEntity.ok(ApiResponse.successWithData(directorDTO, "Lấy dữ liệu đạo diễn với id: " + id + " thành công!", HttpStatus.OK));
    } catch (DirectorException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND, e.getMessage()));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @PostMapping()
  public ResponseEntity<?> createDirector(@Valid @RequestBody DirectorRequest directorRequest) {
    try {
      directorService.create(directorRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.successNoData("Thêm đạo diễn thành công", HttpStatus.CREATED));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @PutMapping()
  public ResponseEntity<?> updateDirector(@Valid @RequestBody DirectorRequest directorRequest) {
    try {
      directorService.update(directorRequest);
      return ResponseEntity.ok(ApiResponse.successNoData("Cập nhật thông tin đạo diễn thành công", HttpStatus.OK));
    } catch (DirectorException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND, e.getMessage()));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteDirector(@PathVariable Long id) {
    try {
      directorService.delete(id);
      return ResponseEntity.ok(ApiResponse.successNoData("Xóa đạo diễn thành công", HttpStatus.OK));
    } catch (DirectorException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND, e.getMessage()));
    } catch (DataAccessException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Xóa đạo diễn thất bại!"));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  private ResponseEntity<?> handleException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống: " + e.getMessage()));
  }
}
