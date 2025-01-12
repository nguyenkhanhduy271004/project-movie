package nguyenduy.local.movie.controllers.movie;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import nguyenduy.local.movie.constant.SystemConstant;
import nguyenduy.local.movie.helper.CustomMessage;
import nguyenduy.local.movie.models.dtos.CommentDTO;
import nguyenduy.local.movie.models.request.CommentRequest;
import nguyenduy.local.movie.models.response.ApiResponse;
import nguyenduy.local.movie.services.interfaces.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/comment")
public class CommentController {

  @Autowired
  private ICommentService commentService;

  @PostMapping
  public ResponseEntity<?> addAndEdit(HttpServletRequest request, @RequestBody CommentRequest commentRequest) {
    try {
      commentService.createAndEdit(request, commentRequest);
      return ResponseEntity.ok(ApiResponse.successWithData(null, CustomMessage.createSuccess(SystemConstant.COMMENT), HttpStatus.OK));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    try {
      commentService.delete(id);
      return ResponseEntity.ok(ApiResponse.successNoData("Comment đã được xóa thành công.", HttpStatus.OK));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Không tìm thấy comment với id: " + id));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi không mong muốn khi xóa comment."));
    }
  }

  @GetMapping
  public ResponseEntity<?> getCommentByMovieId(@RequestParam(name = "movieId") Long movieId) {
    try {
      List<CommentDTO> result = commentService.getCommentsByMovieId(movieId);
      return ResponseEntity.ok(ApiResponse.successWithData(result, CustomMessage.getSuccess(SystemConstant.COMMENT), HttpStatus.OK));
    } catch (Exception e) {
      return handleException(e);
    }
  }


  private ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, SystemConstant.INTERNAL_SERVER_ERROR));
  }

}
