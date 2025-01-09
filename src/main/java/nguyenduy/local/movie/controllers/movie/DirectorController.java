package nguyenduy.local.movie.controllers.movie;

import jakarta.validation.Valid;
import java.util.List;
import nguyenduy.local.movie.constant.SystemConstant;
import nguyenduy.local.movie.exceptions.DirectorException;
import nguyenduy.local.movie.helper.CustomMessage;
import nguyenduy.local.movie.models.dtos.DirectorDTO;
import nguyenduy.local.movie.models.request.DirectorRequest;
import nguyenduy.local.movie.models.response.ApiResponse;
import nguyenduy.local.movie.services.interfaces.IDirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/director")
public class DirectorController {

  @Autowired
  private IDirectorService directorService;

  @GetMapping
  @PreAuthorize("@appAuthorizer.authorize(authentication, 'GET', this)")
  public ResponseEntity<?> getAllDirectors() {
    try {
      List<DirectorDTO> directors = directorService.findAll();
      return ResponseEntity.ok(
          ApiResponse.successWithData(directors, CustomMessage.getSuccess(SystemConstant.DIRECTOR), HttpStatus.OK));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @GetMapping("{id}")
  @PreAuthorize("@appAuthorizer.authorize(authentication, 'GET', this)")
  public ResponseEntity<?> getDirectorById(@PathVariable("id") Long id) {
    try {
      DirectorDTO directorDTO = directorService.findById(id);
      return ResponseEntity.ok(ApiResponse.successWithData(directorDTO, CustomMessage.getSuccess(SystemConstant.DIRECTOR + " với id: " + id), HttpStatus.OK));
    } catch (DirectorException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND, e.getMessage()));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @PostMapping()
  @PreAuthorize("@appAuthorizer.authorize(authentication, 'CREATE', this)")
  public ResponseEntity<?> createDirector(@Valid @RequestBody DirectorRequest directorRequest) {
    try {
      directorService.create(directorRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.successNoData(CustomMessage.createSuccess(SystemConstant.DIRECTOR), HttpStatus.CREATED));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @PutMapping()
  @PreAuthorize("@appAuthorizer.authorize(authentication, 'UPDATE', this)")
  public ResponseEntity<?> updateDirector(@Valid @RequestBody DirectorRequest directorRequest) {
    try {
      directorService.update(directorRequest);
      return ResponseEntity.ok(ApiResponse.successNoData(CustomMessage.updateSuccess(SystemConstant.DIRECTOR), HttpStatus.OK));
    } catch (DirectorException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND, e.getMessage()));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("@appAuthorizer.authorize(authentication, 'DELETE', this)")
  public ResponseEntity<?> deleteDirector(@PathVariable Long id) {
    try {
      directorService.delete(id);
      return ResponseEntity.ok(ApiResponse.successNoData(CustomMessage.deleteSuccess(SystemConstant.DIRECTOR), HttpStatus.OK));
    } catch (DirectorException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND, e.getMessage()));
    } catch (DataAccessException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ApiResponse.error(HttpStatus.BAD_REQUEST, CustomMessage.deleteFailed(SystemConstant.DIRECTOR)));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  private ResponseEntity<?> handleException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, SystemConstant.INTERNAL_SERVER_ERROR));
  }
}
