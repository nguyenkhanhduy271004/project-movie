package nguyenduy.local.movie.controllers.user;

import java.util.List;
import java.util.Map;
import nguyenduy.local.movie.models.dtos.UserDTO;
import nguyenduy.local.movie.models.entities.Movie;
import nguyenduy.local.movie.models.entities.User;
import nguyenduy.local.movie.models.response.ApiResponse;
import nguyenduy.local.movie.models.response.MessageReponse;
import nguyenduy.local.movie.repositories.MovieRepository;
import nguyenduy.local.movie.repositories.UserRepository;
import nguyenduy.local.movie.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private MovieRepository movieRepository;
  @Autowired
  private JwtService jwtService;


  @GetMapping
  public ResponseEntity<?> profile() {
    String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new RuntimeException("User không tồn tại"));

    UserDTO userDTO = UserDTO
        .builder()
        .id(user.getId())
        .phoneNumber(user.getPhoneNumber())
        .build();

    ApiResponse apiResponse = ApiResponse.successWithData(userDTO, "Lấy dữ liệu user thành công", HttpStatus.OK);
    return ResponseEntity.ok(apiResponse);
  }

  @PostMapping("/movie/purchase")
  public ResponseEntity<?> purchaseMovie(@RequestParam(value = "movieId") Long movieId, @RequestHeader("Authorization") String bearerToken) {
    if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Thiếu hoặc sai định dạng Authorization header"));
    }

    String accessToken = bearerToken.substring(7);
    Long userId = Long.valueOf(jwtService.getUserIdFromJwt(accessToken));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy user"));

    Movie movie = movieRepository.findById(movieId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phim"));

    if (user.getCoin() < movie.getCoin()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Bạn không đủ tiền để mua phim"));
    }

    if (user.getMovieIds().contains(movieId)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Bạn đã mua phim này rồi"));
    }

    user.setCoin(user.getCoin() - movie.getCoin());
    List<Long> movieIds = user.getMovieIds();
    movieIds.add(movieId);
    user.setMovieIds(movieIds);
    userRepository.save(user);

    return ResponseEntity.ok(ApiResponse.successNoData("Mua phim thành công", HttpStatus.OK));
  }

}
